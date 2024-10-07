package com.github.gs.lumu.config

import io.circe.Decoder
import io.circe.config.syntax.durationDecoder
import io.circe.generic.semiauto.deriveDecoder

import scala.concurrent.duration.FiniteDuration

final case class CommitTime(value: FiniteDuration) extends AnyVal

object CommitTime {
  implicit val decoderCommitTime: Decoder[CommitTime] = deriveDecoder
}

final case class CommitNumber(value: Int) extends AnyVal
object CommitNumber {
  implicit val decoderCommitNumber: Decoder[CommitNumber] = deriveDecoder
}

final case class CommitBatchWithin(commitNumber: CommitNumber, commitTime: CommitTime)

object CommitBatchWithin {
  implicit val decoderCommitBatchWithin: Decoder[CommitBatchWithin] = deriveDecoder
}

final case class KafkaConsumer(
  groupId: String,
  topic: String,
  maxConcurrent: Int,
  commitBatchWithin: CommitBatchWithin
)

object KafkaConsumer {
  implicit val decoderKafkaConsumer: Decoder[KafkaConsumer] = deriveDecoder
}

final case class KafkaServer(
  host: String
                            )
object KafkaServer {
  implicit val decoderKafkaServer: Decoder[KafkaServer] = deriveDecoder
}

final case class KafkaConfig(
  server: KafkaServer,
  consumer: KafkaConsumer
)
object KafkaConfig {
  implicit val decoderKafkaConfig: Decoder[KafkaConfig] = deriveDecoder
}
