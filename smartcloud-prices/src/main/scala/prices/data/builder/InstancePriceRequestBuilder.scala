package prices.data.builder

import cats.effect.IO
import org.http4s.headers.{Accept, Authorization}
import org.http4s.{AuthScheme, Credentials, Headers, MediaType, Method, Request, Uri}
import prices.data.InstanceKind
import prices.services.SmartcloudInstanceKindService

object InstancePriceRequestBuilder {
  def build(config: SmartcloudInstanceKindService.Config, kind: InstanceKind): Request[IO] =
    Request[IO](
      method = Method.GET,
      uri = Uri.unsafeFromString(s"${config.baseUri}/instances/${kind.getString}"),
      headers = Headers(
        Authorization(Credentials.Token(AuthScheme.Bearer, config.token)),
        Accept(MediaType.application.json),
      )
    )
}

