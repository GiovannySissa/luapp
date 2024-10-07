package com.github.gs.lumu

import cats.effect._
import com.github.gs.lumu.config.ConsumerAppConfig
import com.github.gs.lumu.consumer.IPTrackerConsumer
import com.github.gs.lumu.domain.algebras.IPTrackerAlg
import com.github.gs.lumu.program.RedisRepository
import fs2.Stream
import io.circe.config.parser

object Consumer extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    build[IO].use(_.compile.drain).as(ExitCode.Success)

  def build[F[_]: Async]: Resource[F, Stream[F, Unit]] =
    for {
      config                            <- Resource.eval(parser.decodePathF[F, ConsumerAppConfig]("lumu-app"))
      implicit0(redis: IPTrackerAlg[F]) <- RedisRepository(config.redis)
      consumer = IPTrackerConsumer.make(config.kafka)
    } yield consumer

}
