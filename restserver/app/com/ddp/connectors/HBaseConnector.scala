package com.ddp.connectors

import java.util.Properties

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType
import it.nerdammer.spark.hbase._

/**
  * Created by eguo on 2/14/18.
  */
class HBaseConnector (val spark: SparkSession, val properties: Properties, val schema: Option[StructType]) extends SparkConnector{
  override  def getDataframe: DataFrame = ???


}

object HBaseConnector{
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
    sparkConf.set("spark.hbase.host", "localhost").setMaster("local[*]").setAppName("test1")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.parallelize(1 to 100)
      .map(i => (i.toString, i+1, "Hello"))

    rdd.toHBaseTable("mytable")
      .toColumns("column1", "column2")
      .inColumnFamily("mycf")
      .save()
  }
}