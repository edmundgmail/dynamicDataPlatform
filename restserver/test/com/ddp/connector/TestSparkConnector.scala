package com.ddp.connector

import java.util.Properties

import com.ddp.connectors.JDBCSparkConnector
import com.ddp.models.{NewDataSourceJDBC, Security}
import com.ddp.utils.Testing
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.math.random


class TestSparkConnector extends Testing{
  val conf = new SparkConf().setMaster("local[*]").setSparkHome("/usr/apache/spark-2.1.0-bin-hadoop2.7")
  val spark = SparkSession.builder().config(conf).getOrCreate()

  it("testing oracle connection") {

    val properties = new Properties()
    properties.setProperty("url", "jdbc:oracle:thin:rbc_ces_user/Password1@localhost:1521:orcl")
    properties.setProperty("query","(select ID, SEDOL from SEC_POSITION_SUMMARY) summary_alias")
    import spark.implicits._
    val conn = new JDBCSparkConnector(spark,properties)
    conn.registerTempTable("test1")
    spark.sql("select * from test1").show(10)

  }

  it("test mysql connection") {
    val properties = new Properties()
    val connectionUrl = "jdbc:mysql://localhost:3306/ddp"

    properties.setProperty("jdbcUrl",connectionUrl)

    properties.setProperty("sql","(select * from student) summary_alias")
    properties.setProperty("user", "root")
    properties.setProperty("password","password")
    import spark.implicits._
    val conn = new JDBCSparkConnector(spark,properties)
    conn.registerTempTable("test1")
    spark.sql("select * from test1").show(10)
  }


  it("test mysql connection 1") {
    val connectionUrl = "jdbc:mysql://localhost:3306/ddp"

    val jdbc = NewDataSourceJDBC(connectionUrl, "com.mysql.jdbc.Driver", "root","password", "(SELECT t.*, s.score  FROM ddp.score s, student t where t.id=s.id) summary_alias")
    import spark.implicits._
    val conn = JDBCSparkConnector(spark, jdbc)
    conn.registerTempTable("test1")
    spark.sql("select * from test1").show(10)
  }
}
