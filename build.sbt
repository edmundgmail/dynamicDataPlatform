import com.typesafe.sbt.packager.docker._
import sbt.Keys.libraryDependencies


name := "dynamicDataPlatform"

version := "0.1"

lazy val `dynamicDataPlatform` = (project in file(".")).enablePlugins(PlayScala,ElasticBeanstalkPlugin,BuildInfoPlugin)

scalaVersion := "2.11.8"


licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))

startYear := Some(2017)

val elastic4sVersion = "6.1.0"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.13",
  "org.reactivemongo" %% "reactivemongo-play-json" % "0.11.14",
  "org.apache.spark" % "spark-core_2.11" % "2.2.1" % "compile",
  "org.apache.spark" % "spark-sql_2.11" % "2.2.1" % "compile",
  "org.codehaus.janino" % "janino" % "3.0.7",
"com.julianpeeters" % "case-class-generator_2.11" % "0.7.1",
  "org.mockito" % "mockito-core" % "1.10.19" % Test,
  "org.scalatestplus.play" % "scalatestplus-play_2.11" % "1.5.1" % Test,
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,

  // for the http client
  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,
  // testing
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test",
  "com.sksamuel.elastic4s" %% "elastic4s-embedded" % elastic4sVersion % "test",
  "org.elasticsearch" %% "elasticsearch-spark-20" % elastic4sVersion,
  "org.elasticsearch" % "elasticsearch" % elastic4sVersion,
  "com.twitter" %% "util-eval" % "6.43.0",
  "com.chuusai"   %% "shapeless"     % "2.3.2",
  "com.oracle" % "ojdbc6" % "11.2.0.4"  from "file:/lib/ojdbc6-11.2.0.4.jar",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "6.2.1.jre8",
  "mysql" % "mysql-connector-java" % "5.1.45",
  ////"spark.jobserver" %% "job-server-api" % "0.8.0" % "provided",
  "com.databricks" % "spark-avro_2.11" % "3.2.0",
  "com.hortonworks" % "shc" % "1.1.2-2.2-s_2.11-SNAPSHOT" from "file:/lib/shc-core-1.1.2-2.2-s_2.11-SNAPSHOT.jar",
   ws,
  filters,
  cache)

// disable documentation generation
sources in(Compile, doc) := Seq.empty

// avoid to publish the documentation artifact
publishArtifact in(Compile, packageDoc) := false

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
//resolvers += Resolver.mavenLocal
resolvers += "Job Server Bintray" at "https://dl.bintray.com/spark-jobserver/maven"

net.virtualvoid.sbt.graph.Plugin.graphSettings

javaOptions in test += "-ea"

findbugsSettings

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-encoding", "UTF-8")

routesGenerator := InjectedRoutesGenerator

maintainer in Docker := "Edmund Guo <edmundguo@gmail.com>"
dockerExposedPorts := Seq(9000)
dockerBaseImage := "java:latest"

// BuildInfoPlugin
buildInfoPackage := "build"
