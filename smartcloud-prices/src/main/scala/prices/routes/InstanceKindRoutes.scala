package prices.routes

import cats.implicits._
import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import prices.data.InstanceKind
import prices.routes.protocol._
import prices.services.InstanceKindService

final case class InstanceKindRoutes[F[_]: Sync](instanceKindService: InstanceKindService[F]) extends Http4sDsl[F] {

  val prefix = "/instance-kinds"

  implicit val instanceKindResponseEncoder = jsonEncoderOf[F, InstanceKindResponse]

  implicit val instancePriceResponseEncoder = jsonEncoderOf[F, InstancePriceResponse]

  private val get: HttpRoutes[F] = HttpRoutes.of {
    case GET -> Root =>
      instanceKindService.getAll().flatMap(kinds => Ok(InstanceKindResponse(kinds)))
    case GET -> Root / kind / "price" =>
      instanceKindService.get(InstanceKind(kind)).flatMap(price => Ok(InstancePriceResponse(price)))
  }

  def routes: HttpRoutes[F] =
    Router(
      prefix -> get
    )

}
