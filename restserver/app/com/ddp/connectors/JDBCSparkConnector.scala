package com.ddp.connectors

import java.util.Properties

import com.ddp.models.{CustomRow, NewDataSourceJDBC}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

class JDBCSparkConnector(val spark: SparkSession, val properties: Properties) extends SparkConnector{

  val df = {
    spark.read.jdbc(properties.getProperty("jdbcUrl"), properties.getProperty("sql"), properties)
  }

  def getDataframe: DataFrame = df

}

object JDBCSparkConnector {
  def apply(spark: SparkSession, jdbc: NewDataSourceJDBC) = {
    val properties = new Properties
    properties.setProperty("jdbcURL", jdbc.jdbcUrl)
    properties.setProperty("user", jdbc.user)
    properties.setProperty("password", jdbc.password)
    properties.setProperty("sql", jdbc.sql)
    new JDBCSparkConnector(spark, properties)
  }
}
