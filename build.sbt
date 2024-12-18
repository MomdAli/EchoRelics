val scala3Version = "3.5.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Echo Relics",
    version := "0.1.1-SNAPSHOT",
    scalaVersion := scala3Version,
    scalacOptions += "-Ymacro-annotations",
    libraryDependencies ++= {
      // Determine OS version of JavaFX binaries
      lazy val osName = System.getProperty("os.name") match {
        case n if n.startsWith("Linux")   => "linux"
        case n if n.startsWith("Mac")     => "mac"
        case n if n.startsWith("Windows") => "win"
        case _ => throw new Exception("Unknown platform!")
      }
      Seq(
        "org.scalameta" %% "munit" % "1.0.0" % Test,
        "org.scalactic" %% "scalactic" % "3.2.10",
        "org.scalatest" %% "scalatest" % "3.2.10" % Test,
        "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % Test,
        "org.jline" % "jline" % "3.27.1",
        "org.scalafx" %% "scalafx" % "21.0.0-R32",
        "org.scalafx" %% "scalafx-extras" % "0.10.1"
      ) ++ Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
        .map(m => "org.openjfx" % s"javafx-$m" % "23" classifier osName)
    },
    fork := true
  )
