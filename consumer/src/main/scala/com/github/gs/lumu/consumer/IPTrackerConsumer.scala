package com.github.gs.lumu.consumer

import cats.effect.Concurrent
import cats.effect.kernel.Async
import cats.syntax.either._
import com.github.gs.lumu.config.{ConsumerFSettings, KafkaConfig, KafkaConsumer => KafkaConsumerConfig}
import com.github.gs.lumu.dto.IPTrackerDTO
import fs2.Stream
import fs2.kafka._
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.syntax.applicativeError._
import com.github.gs.lumu.domain.IPTracker
import com.github.gs.lumu.domain.algebras.IPTrackerAlg


final class IPTrackerConsumer[
  F[_]: Async : IPTrackerAlg ] private(
  config: KafkaConsumerConfig) {

  def consumeWith(settings: ConsumerSettings[F, String, String]): Stream[F, Unit] =
    KafkaConsumer
      .stream(settings)
      .evalTap(_.subscribeTo(config.topic))
      .flatMap(_.stream)
      .parEvalMap(10) { committable =>
        pull(committable.record.value)
          .flatMap(IPTrackerAlg[F].save(_))
          .handleErrorWith(err => handleErrors(err, committable.record.value))
          .as(committable.offset)
      }
      .through(
        commitBatchWithin(
          config.commitBatchWithin.commitNumber.value,
          config.commitBatchWithin.commitTime.value
        )
      )

  def pull: String => F[IPTracker] = in =>
    IPTrackerDTO(in)
      .flatMap(dto => IPTracker(timestamp = dto.timestamp, deviceIp = dto.device_ip, errorCode = dto.error_code))
      .liftTo[F]

  // this might be bit better ;)
  def handleErrors(err: Throwable, payload: String): F[Unit] =
    Concurrent[F].pure{
      println(s"The command $payload was not able to consume reason: ${err.getMessage}")
    }


}

object IPTrackerConsumer {
  def apply[F[_]: Async : IPTrackerAlg](config: KafkaConfig): IPTrackerConsumer[F] = {
    new IPTrackerConsumer[F](config.consumer)

  }
  def make[F[_]: Async : IPTrackerAlg](config: KafkaConfig): Stream[F, Unit] = {
    for {
      consumerSettings <- Stream.emit(ConsumerFSettings.acquire(config))
      x <- IPTrackerConsumer(config)
        .consumeWith(consumerSettings.withGroupId(config.consumer.groupId))
    } yield x

  }

}
