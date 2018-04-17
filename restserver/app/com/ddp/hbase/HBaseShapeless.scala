package com.ddp.hbase

import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.util.Bytes

import scala.util.control.Exception.catching

class HBaseTable(tableName: String) {
  implicit val table: HBaseTable = this
}

trait Family {
  def familyName: String
  def tableName: String
}

abstract class StandardFamily(override val tableName:String, override val familyName: String) extends Family {
  implicit val family : Family = this
}

sealed trait Column[T] {
  def family: String
  def name: String
  final def apply(result: Result): Option[T] =
    catching(classOf[NullPointerException]).opt(read(result)).getOrElse(None)

  def apply(value: T): ColumnValue[T]
  def read(result: Result): Option[T]
}

case class StringColumn(override val family: String, override val name: String) extends Column[String] {
  override def apply(value: String) = new ColumnValue[String](this, value)
  override def read(result: Result) = Option( Bytes.toString(result.getValue(family.getBytes, name.getBytes)))
}

final class ColumnValue[T](val column: Column[T], value: T) {
  def apply(): T = value
}