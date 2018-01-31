package com.ddp.connector

import java.util.Properties

import com.ddp.connectors.{JDBCSparkConnector}
import com.ddp.models.Security
import com.ddp.utils.Testing
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Encoder


class TestConnector extends Testing{
  val conf = new SparkConf().setAppName(this.getClass.getCanonicalName).setMaster("local[*]")
  val spark = SparkSession.builder().config(conf).getOrCreate()

  it("testing oracle connection") {

    val properties = new Properties()
    properties.setProperty("url", "jdbc:oracle:thin:rbc_ces_user/Password1@localhost:1521:orcl")
    properties.setProperty("query","(select ID, SEDOL from SEC_POSITION_SUMMARY) summary_alias")
    import spark.implicits._
    val conn = new JDBCSparkConnector[Security](spark,properties)
    conn.registerTempTable("test1")
    spark.sql("select * from test1").show(10)

  }
}