package prices.data.builder

import cats.effect.IO
import org.http4s.headers.{Accept, Authorization}
import org.http4s.Uri
import org.http4s.{AuthScheme, Credentials, Headers, MediaType, Method, Request}
import prices.services.SmartcloudInstanceKindService

object InstancesRequestBuilder {
  def build(config: SmartcloudInstanceKindService.Config): Request[IO] =
    Request[IO](
      method = Method.GET,
      uri = Uri.unsafeFromString(s"${config.baseUri}/instances"),
      headers = Headers(
        Authorization(Credentials.Token(AuthScheme.Bearer, config.token)),
        Accept(MediaType.application.json),
      )
    )
}

