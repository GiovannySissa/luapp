package com.github.gs.lumu.domain

import cats.syntax.either._
import enumeratum.{Enum, EnumEntry}

import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.{Instant, ZoneOffset, ZonedDateTime}

sealed trait Timestamp extends EnumEntry {
  val value: String
  def timeValue: ZonedDateTime
}

object Timestamp extends Enum[Timestamp] {

  override def values: IndexedSeq[Timestamp] = findValues

  final case class SecondsTimestamp(value: String) extends Timestamp {
    override def timeValue: ZonedDateTime =
      ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.toLong), ZoneOffset.UTC)
  }
  final case class MillisecondsTimestamp(value: String) extends Timestamp {

    override def timeValue: ZonedDateTime = {
      ZonedDateTime.ofInstant(Instant.ofEpochSecond(value.toLong), ZoneOffset.UTC)
    }
  }
  final case class RawTimestamp(value: String) extends Timestamp {

    override def timeValue: ZonedDateTime =
      ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME)
  }

  def apply(raw: String): Timestamp = {
    val isNum = raw.forall(_.isDigit)

    raw match {
      case v if isNum && v.length > 10 => MillisecondsTimestamp(v)
      case v if isNum                  => SecondsTimestamp(v)
      case _                           => RawTimestamp(raw)
    }
  }

}

// todo to avoid runtime issues we might create specific types instead generics
final case class IPTracker private (
  timestamp: ZonedDateTime,
  deviceIp: String,
  errorCode: String
)

object IPTracker {
  def apply(timestamp: String, deviceIp: String, errorCode: String): Either[UnknownDateFormat, IPTracker] = {
    Either
      .catchOnly[DateTimeParseException] {
        Timestamp(timestamp).timeValue
      }
      .bimap(
        err => UnknownDateFormat(s"the format ${timestamp} is no valid details:${err.getMessage}"),
        zonedTime =>
          new IPTracker(
            timestamp = zonedTime,
            deviceIp = deviceIp,
            errorCode = errorCode
          )
      )

  }

}
