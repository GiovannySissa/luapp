package com.github.gs.lumu.api

import cats.effect.kernel.Async
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.gs.lumu.domain.algebras.IPTrackerAlg
import com.github.gs.lumu.dto.IPCounter
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final class TrackerAPI[F[_]: Async: IPTrackerAlg] extends Http4sDsl[F] {

  val prefixPath: String = "/v1"

  def endpoints: HttpRoutes[F] =
    Router(prefixPath -> routes)

  def routes: HttpRoutes[F] = HttpRoutes.of {
    case GET -> Root / "track-ip"/ ip / "count" =>
      for {
        r  <- IPTrackerAlg[F].getGlobal(ip)
        r1 <- Ok(IPCounter(r))
      } yield r1

  }
}

object TrackerAPI {
  def apply[F[_]: Async: IPTrackerAlg]: TrackerAPI[F] =
    new TrackerAPI[F]
}
