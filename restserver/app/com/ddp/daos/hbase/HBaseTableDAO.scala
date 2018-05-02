package com.ddp.daos.hbase

trait HBaseTableDAO {
  def setSchema(schema: String): Unit
}