package com.ddp.dataplatform

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object DataPlatformCoreService{
  val conf = new SparkConf().setAppName(this.getClass.getCanonicalName).setMaster("local[*]")
  val spark = SparkSession.builder().config(conf).getOrCreate()
}