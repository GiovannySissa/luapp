import org.scalafmt.sbt.ScalafmtPlugin.scalafmtConfigSettings

lazy val core = project
  .settings(
    commonSettings,
    libraryDependencies ++= Dependencies.test,
    name += "-core",
    testSettings,
    coverageExcludedFiles := "<empty>;.*LoggerHandler.*"
  )

lazy val consumer = project
  .settings(
    commonSettings,
    libraryDependencies ++= Dependencies.common ++ Dependencies.test,
    Compile / mainClass := Some("com.github.gs.lumu.Consumer"),
    name += "-consumer",
    testSettings,
    coverageExcludedFiles := "<empty>;.*LoggerHandler.*"
  )
  .dependsOn(
    core % "compile->compile;test->test",
    core % "compile->compile;test->it"
  )
  .enablePlugins(JavaAppPackaging, DockerPlugin, AshScriptPlugin)


lazy val http = project
  .settings(
    commonSettings,
    name += "-http",
    libraryDependencies ++= Dependencies.http,
    Docker / packageName := "lumu-http",
    dockerExposedPorts   := Seq(8080),
    coverageMinimum      := 0.0,
    testSettings,
    Compile / mainClass := Some("com.github.gs.lumu.Http")
  )
  .dependsOn(core % "compile->compile;test->test", core % "compile->compile;test->it")
  .enablePlugins(DockerPlugin, AshScriptPlugin)

lazy val testSettings =
  inConfig(ItConfig)(Defaults.testSettings ++ scalafmtConfigSettings)

lazy val commonSettings = Seq(
  scalaVersion := "2.13.14",
  organization := "com.github.gs",
  Test / fork  := true,
  name         := "lumu",
  version      := "1.0.0",
  resolvers ++=
    Resolver.sonatypeOssRepos("releases") ++ Seq(
      "confluent" at "https://packages.confluent.io/maven/"
    ),
  coverageMinimum               := 90,
  coverageFailOnMinimum         := true,
  coverageHighlighting          := true,
  ThisBuild / scalafmtOnCompile := true,
  wartremoverErrors ++= OwnWarts.all,
  libraryDependencies ++= Dependencies.common,
  // dockerRepository    := Some("registry.gitlab.com/gsissa/image-registry-repo"),
  Docker / daemonUser := "daemon",
  dockerBaseImage     := "adoptopenjdk/openjdk11:alpine-jre",
  dockerExposedPorts  := Seq(9999),
  addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
)
lazy val ItConfig = config("it") extend Test

addCommandAlias("validate", ";clean;update;compile;scalafmtCheck;scalafmtSbtCheck;coverage;test;it:test;coverageReport")
