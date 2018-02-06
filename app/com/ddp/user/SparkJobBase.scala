package com.ddp.user

import org.apache.spark.sql.SparkSession

/**
  * Created by eguo on 2/5/18.
  */
trait SparkJobBase {
  def runJob(spark: SparkSession)
}