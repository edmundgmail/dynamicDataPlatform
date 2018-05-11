package com.ddp.models

import org.spark_project.jetty.util.security.Credential
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

object DataSourceType extends Enumeration {
  val JDBC = Value("JDBC")
  val LOCALFILE = Value("LOCALFILE")
  val HIVE = Value("HIVE")
  val HBASE = Value("HBASE")
}

case class NewDataSourceRequest(val name: String, val sType: String, val description : String, request: String)

object NewDataSourceRequest{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def newDataSourceRequest = Json.format[NewDataSourceRequest]
}

trait NewDataSource
case class HBaseCredential()
case class NewDataSourceJDBC(jdbcUrl: String, driver: String, user: String, password: String, sql: String) extends NewDataSource
case class NewDataSourceHBase(credential: String, tableName: String, filters: List[String] = List.empty) extends NewDataSource

object NewDataSource{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit val newDataSourceHBase = Json.format[NewDataSourceHBase]
  implicit val newDataSourceHBaseFormat: Reads[NewDataSourceHBase] = {
    ((__ \ "credential").read[String] and
      (__ \ "tableName").read[String] and
      (__ \ "filters").read[List[String]])(NewDataSourceHBase.apply _)
  }
  implicit val newDataSourceJDBC = Json.format[NewDataSourceJDBC]
  implicit val newDataSourceJDBCFormat : Reads[NewDataSourceJDBC] = {
    ((__ \ "jdbcUrl").read[String] and
      (__ \ "driver").read[String] and
      (__ \ "user").read[String] and
      (__ \ "password").read[String] and
      (__ \ "sql").read[String])(NewDataSourceJDBC.apply _)
  }

}