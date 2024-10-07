package com.github.gs.lumu.config

import com.github.gs.lumu.infrastructure.repository.RedisConfig
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class ConsumerAppConfig(
  redis: RedisConfig,
  kafka: KafkaConfig
)

object ConsumerAppConfig {
  implicit val decoderConsumerAppConfig: Decoder[ConsumerAppConfig] = deriveDecoder
}
