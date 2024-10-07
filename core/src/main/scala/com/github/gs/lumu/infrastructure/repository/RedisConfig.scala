package com.github.gs.lumu.infrastructure.repository

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.config.syntax.durationDecoder

import scala.concurrent.duration.FiniteDuration

final case class RedisConfig(
  host: String,
  ttl: FiniteDuration
)

object RedisConfig {
  implicit val decoderRedisConfig: Decoder[RedisConfig] = deriveDecoder
}
