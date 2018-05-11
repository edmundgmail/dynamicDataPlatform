package com.ddp.daos.hbase

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

trait HBaseTableDAO {
  def setSchema(schema: String): Unit

  def getDefaultSchema(tableName: String): String

  def getDataFrame(tableName: String): DataFrame

  def saveDataFrame(tableName: String, dataFrame: DataFrame): Unit

  def getRDD[T](tableName: String) : RDD[T]
  def saveRDD[T] (tableName: String, rdd: RDD[T]): Unit
}