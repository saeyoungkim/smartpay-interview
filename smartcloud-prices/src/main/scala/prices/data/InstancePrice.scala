package prices.data

import io.circe.{Decoder, HCursor}

import java.time.ZonedDateTime

final case class InstancePrice(kind: InstanceKind, price: Double, timestamp: ZonedDateTime)

object InstancePrice {
  implicit val instancePriceDecoder: Decoder[InstancePrice] = new Decoder[InstancePrice] {
    final def apply(c: HCursor): Decoder.Result[InstancePrice] =
      for {
        kind <- c.downField("kind").as[String]
        price <- c.downField("price").as[Double]
        timestamp <- c.downField("timestamp").as[ZonedDateTime]
      } yield {
        new InstancePrice(InstanceKind(kind), price, timestamp)
      }
  }
}