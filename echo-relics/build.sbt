val scala3Version = "3.5.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Echo Relics",
    version := "0.1.1-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test,
    libraryDependencies += "org.mockito" % "mockito-core" % "3.11.2" % Test,
    libraryDependencies += "org.scalatestplus" %% "mockito-5-12" % "3.2.19.0" % Test
  )
