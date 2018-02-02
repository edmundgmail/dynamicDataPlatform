package com.ddp.connectors

import java.util.Properties

import com.ddp.models.CustomRow
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.Encoder

class JDBCSparkConnector[T <: CustomRow: Encoder](val spark: SparkSession, val properties: Properties) extends SparkConnector[T]{

  val df = {
    spark.read.jdbc(url = properties.getProperty("url"), properties.getProperty("query"), properties)
  }

  def getDataframe: DataFrame = df

}
