package com.ddp.user123
import com.ddp.connectors.FileConnector
import com.ddp.userapi.SparkJobApi
import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}


/**
  * Created by eguo on 2/9/18.
  */

class TestCsvFile extends SparkJobApi {

  type JobOutput = List[Row]

  def runJob(spark: SparkSession): JobOutput = {
    val conn = FileConnector("restserver/test/resources/sample.csv", "csv", spark)
    conn.registerTempTable("temp1")
    val filtered = spark.sql("select name from temp1")
    conn.df.rdd.collect.toList
  }
}

object TestCsvFile {
  def main(args:Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkJobTest")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val test = new TestCsvFile
    val s = test.runJob(spark)
    println(s"result=${s.mkString(",")}")
  }

}

