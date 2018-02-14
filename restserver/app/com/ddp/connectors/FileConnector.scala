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

class FileConnector(val spark: SparkSession, val properties: Properties, val ddl: Option[String])extends SparkConnector {
  val t = properties.getProperty("format")
  val path = properties.getProperty("path")
  val schema = ddl match {
    case Some(x) => Some(StructType.fromDDL(x))
    case _ => None
  }

  val df = t match {
    case "json" => if(schema.isDefined) spark.read.schema(schema.get).json(path) else spark.read.json(path)
    case "csv" => if(schema.isDefined) spark.read.schema(schema.get).csv(path) else  spark.read.csv(path)
    case "avro" =>  spark.read.format("com.databricks.spark.avro").load(path)
   }

  def getDataframe: DataFrame = df
}

object FileConnector{
  def apply[T](path:String, format: String, spark: SparkSession, ddl: Option[String] = None) = {

    val name      = ClassName("Person")
    val namespace = ClassNamespace(Some("mypackage"))
    val fieldData = ClassFieldData( List(FieldData("name", typeOf[String]), FieldData("age", typeOf[Int])) )
    val classData = ClassData(namespace, name, fieldData)

    val dcc = new DynamicCaseClass(classData)

    val properties = new Properties()
    properties.setProperty("path", path)
    properties.setProperty("format", format)
    new FileConnector(spark, properties, ddl)
  }
}
