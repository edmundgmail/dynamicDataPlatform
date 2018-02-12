package com.ddp.user
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import com.ddp.userapi.SparkJobApi


/**
  * Created by eguo on 2/9/18.
  */

case class Student(name: String, age: Int)

class MyClass5 extends SparkJobApi {

  type JobOutput =  List[Student]

  def runJob(spark: SparkSession) : JobOutput = {
    List(Student("edmund", 1))
  }
}

object SparkJobTest {

  def main(args:Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkJobTest")
    val spark = SparkSession.builder().config(conf).getOrCreate()

     val class5 = new MyClass5

     println(class5.runJob(spark).mkString(","))
  }
}
