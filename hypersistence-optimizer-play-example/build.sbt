lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    name := """hypersistence-optimizer-play-example""",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      guice,
      javaJpa,
      "com.h2database" % "h2" % "1.4.200",
      "org.hibernate" % "hibernate-core" % "5.4.14.Final",
      "io.hypersistence" % "hypersistence-optimizer" % "2.1.0",
      javaWs % "test",
      "org.awaitility" % "awaitility" % "4.0.1" % "test",
      "org.assertj" % "assertj-core" % "3.14.0" % "test",
      "org.mockito" % "mockito-core" % "3.1.0" % "test",
    ),
    Test / testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v"),
    scalacOptions ++= List("-encoding", "utf8", "-deprecation", "-feature", "-unchecked"),
    javacOptions ++= List("-Xlint:unchecked", "-Xlint:deprecation", "-Werror"),
    PlayKeys.externalizeResourcesExcludes += baseDirectory.value / "conf" / "META-INF" / "persistence.xml"
  )

resolvers += (
  "Local Maven Repository".at(s"file:///${Path.userHome.absolutePath}/.m2/repository")
)

Keys.fork in Test := false