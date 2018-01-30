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
  def registerTempTable(name:String) : Boolean
}
