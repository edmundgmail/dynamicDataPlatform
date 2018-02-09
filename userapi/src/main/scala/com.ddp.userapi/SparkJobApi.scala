package com.ddp.userapi

import org.apache.spark.sql.SparkSession

/**
  * Created by eguo on 2/5/18.
  */
trait SparkJobApi {
  type JobOutput
  type JobData
  def runJob(spark: SparkSession) : JobOutput
}
