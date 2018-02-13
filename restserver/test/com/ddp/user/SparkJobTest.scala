package com.ddp.user
import com.ddp.jarmanager.ScalaSourceCompiler
import com.ddp.models.CodeSnippet
import com.ddp.userapi.SparkJobApi
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession


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

    val name = "com.ddp.user.MyClass5"
    val code =
      """
        |package com.ddp.user
        |import org.apache.spark.sql.SparkSession
        |import com.ddp.userapi.SparkJobApi
        |
        |case class Student(name: String, age: Int)
        |
        |class MyClass5 extends SparkJobApi {
        |
        |  type JobOutput =  List[Student]
        |
        |  def runJob(spark: SparkSession) : JobOutput = {
        |    val ret = List(Student("edmund", 1))
        |    println(ret)
        |    ret
        |  }
        |}
      """.stripMargin
    val snippets = CodeSnippet(name, code)
    ScalaSourceCompiler.compile(spark, snippets)
    val x = ScalaSourceCompiler.run(name)(spark)
    //val y = Await.ready(x, Duration.Inf)
    println(x)
  }
}
