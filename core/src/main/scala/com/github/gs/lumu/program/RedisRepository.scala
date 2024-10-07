package com.github.gs.lumu.program

import cats.effect.Async
import cats.effect.kernel.Resource
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.option._
import com.github.gs.lumu.domain.IPTracker
import com.github.gs.lumu.domain.algebras.IPTrackerAlg
import com.github.gs.lumu.infrastructure.repository.RedisConfig
import dev.profunktor.redis4cats.codecs.Codecs
import dev.profunktor.redis4cats.codecs.splits._
import dev.profunktor.redis4cats.connection.RedisClient
import dev.profunktor.redis4cats.data.RedisCodec
import dev.profunktor.redis4cats.effect.Log.Stdout.instance
import dev.profunktor.redis4cats.effects.SetArg.Ttl
import dev.profunktor.redis4cats.effects.SetArgs
import dev.profunktor.redis4cats.{Redis, RedisCommands}

final class RedisRepository[F[_]: Async] private (config: RedisConfig)(redis: RedisCommands[F, String, Long])
  extends IPTrackerAlg[F] {

  override def save(in: IPTracker): F[Unit] = {
    for {
      count <- getGlobal(in.deviceIp)
      _     <- redis.set(in.deviceIp, count + 1, setArgs = SetArgs(ttl = Ttl.Ex(config.ttl)))
    } yield {
      ()
    }
  }

  override def getGlobal(ip: String): F[Long] =  redis.get(ip).map(_.orEmpty)
}

object RedisRepository {
  def apply[F[_]: Async](config: RedisConfig): Resource[F, IPTrackerAlg[F]] = {
    val longCodec: RedisCodec[String, Long] =
      Codecs.derive(RedisCodec.Utf8, stringLongEpi)

    RedisClient[F]
      .from(config.host)
      .flatMap(Redis[F].fromClient(_, longCodec))
      .map(redis => new RedisRepository[F](config)(redis))
  }
}
