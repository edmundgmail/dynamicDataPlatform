package com.ddp.connectors
import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}
/**
  * Created by eguo on 2/17/18.
  */
class KafkaConnector (val spark: SparkSession, val properties: Properties) extends SparkConnector{


  def getDataframe: DataFrame =
    spark.readStream.format("kafka")
    .option(KafkaConnector.BROKERS ,properties.getProperty(KafkaConnector.BROKERS))
    .option(KafkaConnector.TOPIC, properties.getProperty(KafkaConnector.TOPIC))
    .load()

}

object KafkaConnector{
  val BROKERS = "kafka.bootstrap.servers"
  val TOPIC = "subscribe"

  def apply(spark: SparkSession, brokers: String, topic: String) = {
    val properties = new Properties
    properties.setProperty(BROKERS, brokers)
    properties.setProperty(TOPIC, topic)
    new KafkaConnector(spark, properties)
  }

}