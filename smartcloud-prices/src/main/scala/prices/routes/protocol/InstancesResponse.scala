package prices.routes.protocol

import io.circe.Decoder
import io.circe.generic.semiauto

object InstancesResponse {
  implicit val instancesDecoder: Decoder[List[String]] = semiauto.deriveDecoder[List[String]]
}