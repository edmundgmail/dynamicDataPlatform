package com.ddp.userapi
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession


class A extends SparkJobApi {
  override type JobOutput = Int
  override def runJob(spark: SparkSession): JobOutput = 5
}

object SparkJobApiTest {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkJobTest")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    val a = this.getClass.getClassLoader.loadClass("com.ddp.userapi.A").newInstance.asInstanceOf[SparkJobApi]
    val result = a.runJob(spark)
    println(result)
  }
}
