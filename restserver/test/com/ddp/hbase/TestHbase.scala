package com.ddp.hbase

import com.ddp.daos.hbase.HbaseMetaData
import com.ddp.utils.Testing

class TestHbase extends Testing{
  it("test connect") {
    val tables = HbaseMetaData.getTableNames(".*")
    println(s"tables=${tables.mkString(",")}")
  }

  it("test list table families") {
    val columns = HbaseMetaData.getColumnFamilies("test")
    println(s"columnsFamilies=${columns.mkString(",")}")
  }

  it("test list tables columns") {
    val columns = HbaseMetaData.getColumns("test")
    println(s"columns=${columns.mkString(",")}")
  }
}
