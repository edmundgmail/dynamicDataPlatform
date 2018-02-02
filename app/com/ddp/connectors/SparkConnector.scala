package com.ddp.connectors

import java.util.Properties

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

trait SparkConnector[T] {
  val spark: SparkSession
  val properties : Properties
  //def getRDD[T]: RDD[T]
  //def getDataset: Dataset[T]
  def getDataframe: DataFrame
  //def getDataset: Dataset[T] = getDataframe.as[T]

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
