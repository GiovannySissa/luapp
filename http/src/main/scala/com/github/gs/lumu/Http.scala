package com.github.gs.lumu

import cats.effect.kernel.Async
import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.comcast.ip4s.{Host, Port}
import com.github.gs.lumu.api.TrackerAPI
import com.github.gs.lumu.config.HttpAppConfig
import com.github.gs.lumu.domain.algebras.IPTrackerAlg
import com.github.gs.lumu.program.RedisRepository
import fs2.io.net.Network
import io.circe.config.parser
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

object Http extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    make[IO]
      .use(_ => IO.never)
      .as(ExitCode.Success)

  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  def make[F[_]: Async: Network]: Resource[F, Server] = {
    for {
      config                            <- Resource.eval(parser.decodePathF[F, HttpAppConfig]("lumu-app"))
      implicit0(redis: IPTrackerAlg[F]) <- RedisRepository(config.redis)
      httpApp                           <- Resource.eval(Async[F].pure(TrackerAPI[F]))
      server <- EmberServerBuilder
        .default[F]
        .withHost(Host.fromString(config.http.host).get)
        .withHttpApp(httpApp.endpoints.orNotFound)
        .withPort(Port.fromString(config.http.port).get)
        .build
    } yield server
  }
}
