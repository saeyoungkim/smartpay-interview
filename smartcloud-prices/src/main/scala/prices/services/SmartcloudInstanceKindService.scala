package prices.services

import cats.implicits._
import cats.effect._
import io.circe.parser.decode
import org.http4s.ember.client.EmberClientBuilder
import prices.data._
import cats.effect.unsafe.IORuntime
import org.http4s.InvalidMessageBodyFailure
import org.http4s.circe.jsonOf
import prices.data.builder.{InstancePriceRequestBuilder, InstancesRequestBuilder}

import scala.collection.mutable

object SmartcloudInstanceKindService {

  final case class Config(
      baseUri: String,
      token: String
  )

  private final val priceMemo = mutable.HashMap[InstanceKind, InstancePrice]()

  def getPriceFromMemo(kind: InstanceKind): Option[InstancePrice] = priceMemo.get(kind)
  def insertToMemo(kind: InstanceKind, price: InstancePrice): Option[InstancePrice] = priceMemo.put(kind, price)

  private final val client = EmberClientBuilder
    .default[IO]
    .build

  implicit val runtime: IORuntime = IORuntime.global

  def make[F[_]: Concurrent](config: Config): InstanceKindService[F] = new SmartcloudInstanceKindService(config)

  private final class SmartcloudInstanceKindService[F[_]: Concurrent](
      config: Config
  ) extends InstanceKindService[F] {

    override def getAll(): F[List[InstanceKind]] =
        client
          .use { client =>
            client.expect[String](InstancesRequestBuilder.build(config))
          }
          .map(body => decode[List[String]](body) match {
            case Right(v) => v
            case Left(error) => throw InvalidMessageBodyFailure(error.getMessage)
          })
          .map(kinds => kinds.map(kind => InstanceKind(kind)))
          .unsafeRunSync()
          .pure[F]

    override def get(kind: InstanceKind): F[InstancePrice] =
      client
        .use { client =>
          client.expect[InstancePrice](InstancePriceRequestBuilder.build(config, kind))(jsonOf[IO, InstancePrice])
        }
        .map(price => insertToMemo(kind, price).getOrElse(price))
        .unsafeRunSync()
        .pure[F]
  }

}
