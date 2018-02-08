package com.ddp.dataplatform

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

object DataPlatformCoreService{
  val conf = new SparkConf().setAppName(this.getClass.getCanonicalName).setMaster("local[*]")
  val spark = SparkSession.builder().config(conf).getOrCreate()
  val ssc = new StreamingContext(spark.sparkContext, Seconds(1))


}