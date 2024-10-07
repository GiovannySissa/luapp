package com.github.gs.lumu.domain.algebras

import com.github.gs.lumu.domain.IPTracker

trait IPTrackerAlg [F[_]] {
  def save(in: IPTracker): F[Unit]
  def getGlobal(ip: String): F[Long]
}

object IPTrackerAlg {
  def apply[F[_]: IPTrackerAlg]: IPTrackerAlg[F] = implicitly
}