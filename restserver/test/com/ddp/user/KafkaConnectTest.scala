package com.ddp.user

import com.ddp.connectors.KafkaConnector
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Created by eguo on 2/17/18.
  */
object KafkaConnectTest {
  def main(args: Array[String]) = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkJobTest")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val conn = KafkaConnector(spark,  "localhost:9092", "foo")
    conn.getDataframe.printSchema()
  }
}
