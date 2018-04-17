import sbt.Keys.{javacOptions, libraryDependencies}

val repositories = Seq("scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + ".m2/repository")

name := "dynamicDataPlatform"

version := "0.1"
organization in ThisBuild := "com.ddp"
scalaVersion in ThisBuild := "2.11.8"

lazy val versions = Map(
  "confluent" -> "3.3.1",
  "hbase" -> "1.3.0",
  "hadoop" -> "2.7.1",
  "jackson" -> "2.8.4",
  "spark" -> "2.2.1",
  "elasticsearch" -> "6.1.0",
  "kafka"->"0.11.0.2"
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

val sparkCoreProvided = "org.apache.spark" % "spark-core_2.11" % versions("spark") % "provided"   //from "file:/lib/spark-core_2.11-2.2.1.jar"
val sparkSqlProvided = "org.apache.spark" % "spark-sql_2.11" % versions("spark")  %  "provided" //from "file:/lib/spark-sql_2.11-2.2.1.jar"
val sparkStreamProvided = "org.apache.spark" % "spark-streaming_2.11" % versions("spark") % "provided" //from "file:/lib/spark-streaming_2.11-2.2.1.jar"
val sparkHiveProvided = "org.apache.spark" % "spark-hive_2.11" % versions("spark") % "provided"

val sparkCore = "org.apache.spark" % "spark-core_2.11" % versions("spark") % "compile"
val sparkSql = "org.apache.spark" % "spark-sql_2.11" % versions("spark")  % "compile"
val sparkStream = "org.apache.spark" % "spark-streaming_2.11" % versions("spark")  % "compile"
val sparkHive = "org.apache.spark" % "spark-hive_2.11" % versions("spark")  % "compile"
val sparkKafka = "org.apache.spark" % "spark-sql-kafka-0-10_2.11" % versions("spark") % "compile"
val sparkKafkaStreaming = "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % versions("spark") % "compile"
val kafka = "org.apache.kafka" % "kafka_2.11" % versions("kafka") % "compile"

val hbaseClient = "org.apache.hbase" % "hbase-client" % versions("hbase")
val hbaseCommon = "org.apache.hbase" % "hbase-common" % versions("hbase")
val hbaseCommonTest = "org.apache.hbase" % "hbase-common" % versions("hbase") % "test" classifier "tests"
val hbaseServer = "org.apache.hbase" % "hbase-server" % versions("hbase") % "test"
val hbaseServerTest = "org.apache.hbase" % "hbase-server" % versions("hbase") % "test" classifier "tests"
val hbaseHdpCompat = "org.apache.hbase" % "hbase-hadoop-compat" % versions("hbase") % "test"
val hbaseHdpCompatTest = "org.apache.hbase" % "hbase-hadoop-compat" % versions("hbase") % "test" classifier "tests"
val hbaseHdp2Compat = "org.apache.hbase" % "hbase-hadoop2-compat" % versions("hbase") % "test"
val hbaseHdp2CompatTest = "org.apache.hbase" % "hbase-hadoop2-compat" % versions("hbase") % "test" classifier "tests"

val userapiDependencies = Seq(sparkCoreProvided,sparkSqlProvided,sparkStreamProvided, sparkHiveProvided)

val restDependencies = Seq(
  sparkCore,sparkSql,sparkStream, sparkHive,sparkKafka, sparkKafkaStreaming, kafka,
  hbaseClient, hbaseCommon,
  hbaseServer, hbaseServerTest, hbaseCommonTest, hbaseHdpCompat, hbaseHdpCompatTest, hbaseHdp2Compat,
  hbaseHdp2CompatTest,
  "org.mongodb.spark" % "mongo-spark-connector_2.11" % versions("spark") % "compile",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.13",
  "org.reactivemongo" %% "reactivemongo-play-json" % "0.11.14" % "compile",
  "org.mockito" % "mockito-core" % "1.10.19" % Test,
  "org.scalatestplus.play" % "scalatestplus-play_2.11" % "1.5.1" % Test,
  "com.twitter" %% "util-eval" % "6.43.0",
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.0-akka-2.4.x",
  "com.google.guava" % "guava" % "16.0.1",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "6.2.1.jre8",
  "mysql" % "mysql-connector-java" % "5.1.45",
  "com.databricks" % "spark-avro_2.11" % "3.2.0",
  jdbc,
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
    .dependsOn(userapi)
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

