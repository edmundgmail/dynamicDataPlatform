package com.ddp.connectors

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by eguo on 2/1/18.
  */
class FileConnector[T](val spark: SparkSession, val properties: Properties) extends SparkConnector[T]{
  val t = properties.getProperty("format")
  val path = properties.getProperty("path")
  val df = t match {
    case "json" => spark.read.json(path)
    case "csv" => spark.read.csv(path)
    case "text" => spark.read.text(path)
    case "avro" => spark.read.format("com.databricks.spark.avro").load(path)
   }

  def getDataframe: DataFrame = df
}

object FileConnector{
  def apply[T](path:String, format: String, spark: SparkSession) = {
    val properties = new Properties()
    properties.setProperty("path", path)
    properties.setProperty("format", format)
    new FileConnector[T](spark, properties)
  }
}
