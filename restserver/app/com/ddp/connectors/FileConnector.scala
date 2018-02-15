package com.ddp.connectors

import java.util.Properties

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import ste.StructTypeEncoder
import ste.StructTypeEncoder._
import com.julianpeeters.caseclass.generator._
import scala.reflect.runtime.universe._

/**
  * Created by eguo on 2/1/18.
  */

class FileConnector(val spark: SparkSession, val properties: Properties, val schema: Option[StructType])extends SparkConnector {
  val t = properties.getProperty("format")
  val path = properties.getProperty("path")

  val df = t match {
    case "json" => if(schema.isDefined) spark.read.schema(schema.get).json(path) else spark.read.json(path)
    case "csv" => if(schema.isDefined) spark.read.schema(schema.get).csv(path) else  spark.read.csv(path)
    case "avro" =>  spark.read.format("com.databricks.spark.avro").load(path)
   }

  def getDataframe: DataFrame = df
}

object FileConnector{
  def apply(path:String, format: String, spark: SparkSession, schema: Option[StructType]) : FileConnector = {

    val properties = new Properties()
    properties.setProperty("path", path)
    properties.setProperty("format", format)

    new FileConnector(spark, properties, schema)
  }

  def apply(path: String, format: String, spark: SparkSession, ddl: String)  : FileConnector = {
      apply(path, format,spark, Some(StructType.fromDDL(ddl)))
  }

  def apply(path: String, format: String ,spark:SparkSession) : FileConnector = {
    apply(path, format, spark, None)
  }
}
