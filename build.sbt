import sbt.Keys.{javacOptions, libraryDependencies}

val repositories = Seq("scalaz-bintray" at "https://dl.bintray.com/scalaz/releases")
name := "dynamicDataPlatform"

version := "0.1"
organization in ThisBuild := "com.ddp"
scalaVersion in ThisBuild := "2.11.8"

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

val elastic4sVersion = "6.1.0"

lazy val restserver = project
  .enablePlugins(PlayScala,ElasticBeanstalkPlugin,BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(
    version := "0.1",
    libraryDependencies ++= Seq(
      "org.reactivemongo" %% "play2-reactivemongo" % "0.11.13",
      "org.reactivemongo" %% "reactivemongo-play-json" % "0.11.14" % "compile",
      "org.apache.spark" % "spark-core_2.11" % "2.2.1" % "compile",
      "org.apache.spark" % "spark-sql_2.11" % "2.2.1" % "compile",
      "org.apache.spark" % "spark-streaming_2.11" % "2.2.1",
      "org.mongodb.spark" % "mongo-spark-connector_2.11" % "2.2.1",
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
      "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.0-akka-2.4.x",
      ws,
      filters,
      cache)
  )

lazy val  root = project.in(file("."))
  .dependsOn(restserver)
  .aggregate(restserver)
