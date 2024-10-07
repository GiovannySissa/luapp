package com.github.gs.lumu.config

import com.github.gs.lumu.infrastructure.repository.RedisConfig
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class HttpAppConfig(
  redis: RedisConfig,
  http: HttpConfig
)
object HttpAppConfig {
  implicit val decoderHttpConfig: Decoder[HttpAppConfig] = deriveDecoder
}
