import sbt.*

object Versions {
  lazy val cats        = "2.12.0"
  lazy val catsEffects = "3.5.4"
  lazy val mouse       = "1.3.1"
  lazy val slf4j       = "2.0.12"
  lazy val circeConfig = "0.10.1"
  lazy val circe       = "0.14.9"
  lazy val fs2         = "3.10.2"
  lazy val munit       = "1.0.0"
  lazy val redis4cats  = "1.7.1"
  lazy val kafkaFs2    = "3.5.1"
  lazy val enumeratum  = "1.7.2"
  lazy val http4s = "0.23.28"
}

object Dependencies {

  lazy val common: Seq[ModuleID] = Seq(
    "org.typelevel"   %% "cats-core"           % Versions.cats withSources () withJavadoc (),
    "org.typelevel"   %% "cats-effect"         % Versions.catsEffects withSources () withJavadoc (),
    "org.typelevel"   %% "mouse"               % Versions.mouse withSources () withJavadoc (),
    "org.slf4j"        % "slf4j-api"           % Versions.slf4j,
    "io.circe"        %% "circe-config"        % Versions.circeConfig,
    "io.circe"        %% "circe-core"          % Versions.circe,
    "io.circe"        %% "circe-parser"        % Versions.circe,
    "io.circe"        %% "circe-generic"       % Versions.circe,
    "co.fs2"          %% "fs2-core"            % Versions.fs2,
    "dev.profunktor"  %% "redis4cats-effects"  % Versions.redis4cats,
    "dev.profunktor"  %% "redis4cats-log4cats" % Versions.redis4cats,
    "com.github.fd4s" %% "fs2-kafka"           % Versions.kafkaFs2,
    "com.beachape"    %% "enumeratum"          % Versions.enumeratum
  )

  lazy val http: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-ember-client" % Versions.http4s,
    "org.http4s" %% "http4s-ember-server" % Versions.http4s,
    "org.http4s" %% "http4s-dsl" % Versions.http4s,
    "org.http4s" %% "http4s-circe" % Versions.http4s,
  )
  lazy val test: Seq[ModuleID] = Seq(
//    "io.monix"       %% "minitest"      % Versions.miniTest   % s"it,$Test",
//    "io.monix"       %% "minitest-laws" % Versions.miniTest   % s"it,$Test",
//    "org.scalacheck" %% "scalacheck"    % Versions.scalaCheck % s"it,$Test",
//    "org.tpolecat"   %% "doobie-h2"     % Versions.doobie     % s"it,$Test",
    "org.scalameta" %% "munit" % Versions.munit
  )

}
