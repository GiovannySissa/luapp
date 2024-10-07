package com.github.gs.lumu.dto

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

final case class IPCounter(
  count: Long
)

object IPCounter {
  implicit val codec: Codec[IPCounter] = deriveCodec
}
