package com.github.gs.lumu.dto

import io.circe
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.parser._

final case class IPTrackerDTO(
  timestamp: String,
  device_ip: String,
  error_code: String
)

object IPTrackerDTO {
  implicit val decoderIPTrackerDTO: Decoder[IPTrackerDTO] = deriveDecoder

  def apply(in: String): Either[circe.Error, IPTrackerDTO] = {
    decode[IPTrackerDTO](in)
  }

}
