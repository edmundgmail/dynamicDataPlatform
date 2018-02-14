package com.ddp.user
import com.ddp.connectors.FileConnector
import com.ddp.userapi.SparkJobApi
import com.mongodb.spark.MongoSpark
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SparkSession}
import ste.StructTypeEncoder

/**
  * Created by eguo on 2/9/18.
  */
case class Student1(id: Int, name: String, age: Int)

class TestCsvFile extends SparkJobApi {

  type JobOutput = List[Row]

  def runJob(spark: SparkSession): JobOutput = {

    val conn = FileConnector("restserver/test/resources/sample.csv", "csv", spark, None)
    import spark.sqlContext._

    conn.registerTempTable("temp123")
    val filtered = spark.sql("describe temp123")
    filtered.collect().toList
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

