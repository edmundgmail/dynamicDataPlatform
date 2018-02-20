package com.ddp.user

import com.ddp.connectors.FileConnector
import com.ddp.userapi.SparkJobApi
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
case class Security(Date_Time_Stamp: String, Exchange_Rate: String)

class TestJsonFile extends SparkJobApi{
  override type JobOutput = List[Security]
  override def runJob(spark: SparkSession): JobOutput = {
    //val conn = FileConnector("restserver/test/resources/sample.json", "json", spark, "Date_Time_Stamp varchar(100), Exchange_Rate Decimal(10,3)")
    val conn = FileConnector("restserver/test/resources/sample.json", "json", spark)
    conn.registerTempTable("temp123")
    //filtered.collect.toList
    //val rdd: RDD[Row] = conn.df.rdd
    import spark.implicits._

    conn.df.as[Security].collect().toList
    /*
    val readConfig = ReadConfig(Map("uri" -> "mongodb://127.0.0.1/ddp.securities?readPreference=primaryPreferred"))
    val writeConfig = WriteConfig(Map("uri" -> "mongodb://127.0.0.1/ddp.securities"))

    MongoSpark.save(filtered, writeConfig)
    val rdd = MongoSpark.load(spark, readConfig)*/
    //conn.df.as[Security].collect().toList
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


