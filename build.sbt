name := "scala-redis-cluster"

organization := "com.github.peking2"

version := "0.1"

scalaVersion := "2.11.2"

resolvers ++= Seq(
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "com.livestream" %% "scredis" % "2.0.5",
  "commons-codec" % "commons-codec" % "1.9",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomExtra := (
  <url>https://github.com/peking2/scala-redis-cluster.git</url>
    <licenses>
      <license>
        <name>BSD-style</name>
        <url>http://www.opensource.org/licenses/bsd-license.php</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:peking2/scala-redis-cluster.git</url>
      <connection>scm:git:git@github.com:peking2/scala-redis-cluster.git</connection>
    </scm>
    <developers>
      <developer>
        <id>peking2</id>
        <name>Gary Zhao</name>
        <url>https://github.com/peking2</url>
      </developer>
    </developers>)

