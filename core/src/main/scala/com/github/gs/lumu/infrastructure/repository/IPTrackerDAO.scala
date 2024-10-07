package com.github.gs.lumu.infrastructure.repository

import com.github.gs.lumu.domain.IPTracker

final case class IPTrackerDAO(
  timestamp: String,
  deviceIp: String,
  errorCode: String
)

object IPTrackerDAO {

  def apply(dobj: IPTracker): IPTrackerDAO =
    new IPTrackerDAO(
      timestamp = dobj.timestamp.toEpochSecond.toString, deviceIp = dobj.deviceIp, errorCode = dobj.errorCode
    )
}