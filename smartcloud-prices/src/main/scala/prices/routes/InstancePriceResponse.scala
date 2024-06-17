package prices.routes

import io.circe.syntax.EncoderOps
import io.circe.{Encoder, Json}
import prices.data.InstancePrice

final case class InstancePriceResponse(
    instancePrice: InstancePrice,
)

case object InstancePriceResponse {
  implicit val instancePriceEncoder: Encoder[InstancePriceResponse] =
    Encoder.instance[InstancePriceResponse] {
      case InstancePriceResponse(instancePrice) =>
        Json.obj(
          "kind" -> instancePrice.kind.getString.asJson,
          "price" -> instancePrice.price.asJson,
          "timestamp" -> instancePrice.timestamp.asJson
        )
    }
}
