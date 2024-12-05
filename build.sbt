val scala3Version = "3.5.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Echo Relics",
    version := "0.1.1-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "org.scalactic" %% "scalactic" % "3.2.10",
      "org.scalatest" %% "scalatest" % "3.2.10" % Test,
      "org.jline" % "jline" % "3.27.1",
      "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % Test
    )
  )
