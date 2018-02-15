package com.ddp.connectors

import java.util.{Properties, UUID}

import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.WriteConfig
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType
import it.nerdammer.spark.hbase._

/**
  * Created by eguo on 2/14/18.
  */
class HBaseConnector (val spark: SparkSession, val properties: Properties, val schema: Option[StructType]) extends SparkConnector{
  override  def getDataframe: DataFrame = ???


}

object HBaseConnector{
  def main(args: Array[String]): Unit = {
    val tables: Seq[String] = Seq(UUID.randomUUID().toString)
    val columnFamilies: Seq[String] = Seq(UUID.randomUUID().toString, UUID.randomUUID().toString)

    tables foreach {IntegrationUtils.createTable(_, columnFamilies)}

    val sc = IntegrationUtils.sparkContext

    val data = sc.parallelize(1 to 100).map(i => (i.toString, i.toString, i))


    data.toHBaseTable(tables(0))
      .toColumns("column1", columnFamilies(1) + ":column2")
      .inColumnFamily(columnFamilies(0))
      .save()


  }
}