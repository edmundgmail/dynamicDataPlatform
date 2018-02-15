package com.ddp.connectors

import java.util.Properties

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

trait SparkConnector {
  val spark: SparkSession
  val properties : Properties
  def getDataframe: DataFrame

  def registerTempTable(name:String) : Boolean = {
    try{
      getDataframe.createOrReplaceTempView(name)
      true
    }
    catch {
      case _ => false
    }
  }
}
