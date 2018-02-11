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
  "spark" -> "2.1.0",
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


val userapiDependencies = Seq(
  "org.apache.spark" % "spark-core_2.11" % versions("spark") % "compile",
  "org.apache.spark" % "spark-sql_2.11" % versions("spark") % "compile",
  "org.apache.spark" % "spark-streaming_2.11" % versions("spark") % "compile",
  "org.mongodb.spark" % "mongo-spark-connector_2.11" % versions("spark") % "compile",
  "com.oracle" % "ojdbc6" % "11.2.0.4"  from "file:/lib/ojdbc6-11.2.0.4.jar",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "6.2.1.jre8" % "provided",
  "mysql" % "mysql-connector-java" % "5.1.45" % "provided",
  ////"spark.jobserver" %% "job-server-api" % "0.8.0" % "provided",
  "com.databricks" % "spark-avro_2.11" % "3.2.0"  % "provided"
)

val restDependencies = Seq(
  "org.apache.spark" % "spark-core_2.11" % versions("spark") exclude("org.slf4j", "slf4j-api"),
  "org.apache.spark" % "spark-sql_2.11" % versions("spark") % "compile" exclude("org.slf4j", "slf4j-api"),
  "org.apache.spark" % "spark-streaming_2.11" % versions("spark") % "compile",
  "org.mongodb.spark" % "mongo-spark-connector_2.11" % versions("spark") % "compile" exclude("org.slf4j", "slf4j-api"),
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.13" exclude("org.slf4j", "slf4j-api"),
  "org.reactivemongo" %% "reactivemongo-play-json" % "0.11.14" % "compile",
  "org.codehaus.janino" % "janino" % "3.0.7",
  "com.julianpeeters" % "case-class-generator_2.11" % "0.7.1",
  "org.mockito" % "mockito-core" % "1.10.19" % Test,
  "org.scalatestplus.play" % "scalatestplus-play_2.11" % "1.5.1" % Test,
  "com.twitter" %% "util-eval" % "6.43.0",
  "com.chuusai"   %% "shapeless"     % "2.3.2",
  "com.hortonworks" % "shc" % "1.1.2-2.2-s_2.11-SNAPSHOT" from "file:/lib/shc-core-1.1.2-2.2-s_2.11-SNAPSHOT.jar",
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.0-akka-2.4.x",

  "com.oracle" % "ojdbc6" % "11.2.0.4"  from "file:/lib/ojdbc6-11.2.0.4.jar",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "6.2.1.jre8",
  "mysql" % "mysql-connector-java" % "5.1.45",
  ////"spark.jobserver" %% "job-server-api" % "0.8.0" % "provided",
  "com.databricks" % "spark-avro_2.11" % "3.2.0",
    ws,
  filters,
  cache
)

lazy val userapi = project.in(file("userapi"))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(
    version := "0.1",
    libraryDependencies ++= userapiDependencies
  )

lazy val restserver = project.in(file("restserver"))
  .enablePlugins(PlayScala,BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(
    version := "0.1",
    libraryDependencies ++= restDependencies
  )
  .dependsOn(userapi % "compile->compile;test->test")


lazy val  root = project.in(file("."))
  .aggregate(userapi, restserver)

projectDependencies := {
  Seq((projectID in root).value.exclude("org.slf4j", "slf4j-log4j12").exclude("org.slf4j", "slf4j-api"))
}
