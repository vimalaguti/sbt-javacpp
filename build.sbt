lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-javacpp",
    version := "1.15-SNAPSHOT",
    organization := "org.bytedeco",
    scalaVersion := "2.12.7",
    sbtVersion in Global := "1.2.7",
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false
  )

sbtPlugin := true

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.8.1"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xlint", "-Xlog-free-terms")

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomExtra :=
  <url>https://github.com/bytedeco/sbt-javacpp</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>http://opensource.org/licenses/MIT</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:bytedeco/sbt-javacpp.git</url>
      <connection>scm:git:git@github.com:bytedeco/sbt-javacpp.git</connection>
    </scm>
    <developers>
      <developer>
        <id>lloydmeta</id>
        <name>Lloyd Chan</name>
        <url>https://beachape.com</url>
      </developer>
      <developer>
        <id>vimalaguti</id>
        <name>Vittorio Malaguti</name>
      </developer>
    </developers>
