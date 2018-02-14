package com.ddp.user

import com.ddp.connectors.FileConnector
import com.ddp.user123.{Student1, TestCsvFile}
import com.ddp.userapi.SparkJobApi
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.{ReadConfig, WriteConfig}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}

case class Security(Date_Time_Stamp: String, Exchange_Rate: String)

class TestJsonFile extends SparkJobApi{
  override type JobOutput = List[Row]
  override def runJob(spark: SparkSession): JobOutput = {
    val conn = FileConnector[Security]("restserver/test/resources/sample.json", "json", spark)
    conn.registerTempTable("temp123")
    val filtered = spark.sql("describe temp123")
    //filtered.collect.toList

    val readConfig = ReadConfig(Map("uri" -> "mongodb://127.0.0.1/ddp.securities?readPreference=primaryPreferred"))
    val writeConfig = WriteConfig(Map("uri" -> "mongodb://127.0.0.1/ddp.securities"))

    MongoSpark.save(filtered, writeConfig)
    val rdd = MongoSpark.load(spark, readConfig)
    rdd.collect().toList
  }
}

object TestJsonFile {
  def main(args:Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkJobTest")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val test = new TestJsonFile
    val s = test.runJob(spark)
    println(s"result=${s.mkString(",")}")
  }

}


