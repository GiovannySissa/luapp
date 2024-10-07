package com.github.gs.lumu.config


import cats.effect.Async
import fs2.kafka.{AutoOffsetReset, ConsumerSettings, Deserializer}


object ConsumerFSettings {
  def acquire[F[_]: Async](
    config: KafkaConfig
  ): ConsumerSettings[F, String, String] =
    ConsumerSettings(
      keyDeserializer = Deserializer.delegate[F, String] {
        (_: String, data: Array[Byte]) => {
          Option(data).map(d => new String(d, "UTF-8"))
            .getOrElse("")
        }
      },
      valueDeserializer = Deserializer.delegate[F, String] {
        (_: String, data: Array[Byte]) => {
          new String(data, "UTF-8")
        }
      }
    )
      .withAutoOffsetReset(AutoOffsetReset.Latest)
      .withBootstrapServers(config.server.host)


}
