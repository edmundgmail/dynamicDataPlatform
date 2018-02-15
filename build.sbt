import sbt.Keys.{javacOptions, libraryDependencies}

val repositories = Seq("scalaz-bintray" at "https://dl.bintray.com/scalaz/releases")
name := "dynamicDataPlatform"

version := "0.1"
organization in ThisBuild := "com.ddp"
scalaVersion in ThisBuild := "2.11.8"

lazy val versions = Map(
  "confluent" -> "3.3.1",
  "hbase" -> "1.1.2",
  "hadoop" -> "2.7.1",
  "jackson" -> "2.8.4",
  "spark" -> "2.2.1",
  "elasticsearch" -> "6.1.0"
)

lazy val commonSettings = Seq(
  parallelExecution in Test := false,
  fork in Test := true,
  cancelable in Global := true,
  baseDirectory in Test := file("."),
  resolvers ++= repositories,
  publishArtifact in(Compile, packageDoc) := false,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-encoding", "UTF-8"),
  unmanagedClasspath in(Compile, runMain) += baseDirectory.value / "target/generated-sources/java/",
  buildInfoPackage := "build"
)

val sparkCoreProvided = "org.apache.spark" % "spark-core_2.11" % versions("spark") % "provided" //from "file:/lib/spark-core_2.11-2.2.1.jar"
val sparkSqlProvided = "org.apache.spark" % "spark-sql_2.11" % versions("spark")  % "provided"  //from "file:/lib/spark-sql_2.11-2.2.1.jar"
val sparkStreamProvided = "org.apache.spark" % "spark-streaming_2.11" % versions("spark") % "provided" //from "file:/lib/spark-streaming_2.11-2.2.1.jar"

val sparkCore = "org.apache.spark" % "spark-core_2.11" % versions("spark") //from "file:/lib/spark-core_2.11-2.2.1.jar"
val sparkSql = "org.apache.spark" % "spark-sql_2.11" % versions("spark") //from "file:/lib/spark-sql_2.11-2.2.1.jar"
val sparkStream = "org.apache.spark" % "spark-streaming_2.11" % versions("spark") //from "file:/lib/spark-streaming_2.11-2.2.1.jar"

val userapiDependencies = Seq(sparkCoreProvided,sparkSqlProvided,sparkStreamProvided)

val restDependencies = Seq(
  sparkCore,sparkSql,sparkStream,
  "org.mongodb.spark" % "mongo-spark-connector_2.11" % versions("spark") % "compile",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.13",
  "org.reactivemongo" %% "reactivemongo-play-json" % "0.11.14" % "compile",
  "org.mockito" % "mockito-core" % "1.10.19" % Test,
  "org.scalatestplus.play" % "scalatestplus-play_2.11" % "1.5.1" % Test,
  "com.twitter" %% "util-eval" % "6.43.0",
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.0-akka-2.4.x",

  "com.oracle" % "ojdbc6" % "11.2.0.4"  from "file:/lib/ojdbc6-11.2.0.4.jar",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "6.2.1.jre8",
  "mysql" % "mysql-connector-java" % "5.1.45",
  ////"spark.jobserver" %% "job-server-api" % "0.8.0" % "provided",
  "com.databricks" % "spark-avro_2.11" % "3.2.0",
  "com.ddp" % "userapi_2.11" % "0.1" from "file:/lib/userapi_2.11-0.1.jar",
  "com.github.benfradet" %% "struct-type-encoder" % "0.1.0",
  "com.julianpeeters" %% "case-class-generator" % "0.7.1",
  "it.nerdammer.bigdata" % "spark-hbase-connector_2.11" % "1.0.4" from "file:/lib/spark-hbase-connector_2.11.jar",
  "com.hortonworks" % "shc" % "1.1.2-2.2-s_2.11-SNAPSHOT" from "file:/lib/shc-core-1.1.2-2.2-s_2.11-SNAPSHOT.jar",
    ws,
  filters,
  cache
)

lazy val userapi = project.in(file("userapi"))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(
    version := "0.1",
    name := "userapi",
    libraryDependencies ++= userapiDependencies
  )

def excludeJackson(module: ModuleID): ModuleID =
  module.excludeAll(ExclusionRule(organization = "com.fasterxml.jackson.core"))

lazy val restserver = project.in(file("restserver"))
  .enablePlugins(PlayScala,BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(libraryDependencies ~= (_.map(excludeJackson)))
  .settings(
    version := "0.1",
    name := "restserver",
    libraryDependencies ++= restDependencies
  )
  //.dependsOn(userapi % "compile->compile;test->test")


lazy val  root = project.in(file("."))
  .aggregate(userapi, restserver)

