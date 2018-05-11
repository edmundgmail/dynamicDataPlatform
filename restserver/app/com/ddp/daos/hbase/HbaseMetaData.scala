package com.ddp.daos.hbase

import java.io.IOException

import org.apache.hadoop.hbase._
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import scala.collection.JavaConversions._


object HbaseMetaData {
  /** get all Table names **/
  val conf = HBaseConfiguration.create()
  val conn: Connection = ConnectionFactory.createConnection(conf)
  val admin = conn.getAdmin

  @throws[IOException]
  def getTableNames(regex: String) = admin.listTableNames(regex)

  /** get all columns from the table **/
  @throws[IOException]
  def getColumnsInColumnFamily(table: Table, columnFamily: String) : List[String] = {
      val scan = new Scan
      scan.addFamily(Bytes.toBytes(columnFamily))
      val rs = table.getScanner(scan)

      if(rs.iterator().hasNext){
        rs.next().getFamilyMap(Bytes.toBytes(columnFamily)).keySet.map(columnFamily + ":" + new String(_)).toList
      }
      else
        List.empty
  }


  @throws[IOException]
  def getColumnFamilies(hbaseTable: String) : List[String] = {
    val tableName = TableName.valueOf(hbaseTable)
    admin.getTableDescriptor(tableName).getColumnFamilies.map(_.getNameAsString).toList
  }

  @throws[IOException]
  def getColumns(hbaseTable: String) : List[String] = {
    val tableName = TableName.valueOf(hbaseTable)
    val families = admin.getTableDescriptor(tableName).getColumnFamilies.map(_.getNameAsString)
    val table = conn.getTable(tableName)
    families.flatMap(familyName=>getColumnsInColumnFamily(table, familyName)).toList
  }
}