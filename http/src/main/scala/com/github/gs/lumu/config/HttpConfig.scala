package com.github.gs.lumu.config

import io.circe.Decoder
import io.circe.config.syntax.durationDecoder

import io.circe.generic.semiauto.deriveDecoder

import scala.concurrent.duration.FiniteDuration

final case class HttpConfig(
  host: String,
  port: String,
  timeout: FiniteDuration
)

object HttpConfig {
  implicit val decoderHttpConfig: Decoder[HttpConfig] = deriveDecoder
}
