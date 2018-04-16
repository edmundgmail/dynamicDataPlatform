package com.ddp.hbase

import com.ddp.logging.Logging
import com.ddp.utils.Testing
import org.apache.hadoop.hbase.util.Bytes

class TestHBase extends Testing with Logging{
  it("test creating local HBase connection") {
      val hBaseDAO =new HBaseDAO("localhost", "2181", "/hbase")
      val x = hBaseDAO.getRowKeys("t1", 100)
      val y = x.map(Bytes.toString).mkString(",")
      println(s"rowkeys = ${y}")
  }
  it("test the filters") {
    val hBaseDAO =new HBaseDAO("localhost", "2181", "/hbase")
    val x = hBaseDAO.getRowkeysWithFilter("t1", List(MyColumnValueFilter("cf1","name","edmund"), MyColumnValueFilter("cf1","age","12")))
    val y = x.map(Bytes.toString).mkString(",")
    println(s"rowkeys = ${y}")
  }
}
