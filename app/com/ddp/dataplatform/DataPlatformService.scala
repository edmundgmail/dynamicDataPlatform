package com.ddp.dataplatform

import javax.inject.{Inject, Singleton}

import org.apache.spark.sql.SparkSession
import play.api.Logger
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.util.Try

@Singleton
class DataPlatformService @Inject()( ) {
  def ingestCsvFile(sc: SparkSession, tableName: String, inputFilePath: String) = ???
}
