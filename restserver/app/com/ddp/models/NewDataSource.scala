package com.ddp.models

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

case class NewDataSourceJDBC(jdbcUrl: String, driver: String, user: String, password: String, sql: String) extends NewDataSource
case class NewDataSourceHBase() extends NewDataSource

object NewDataSource{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def newDataSourceJDBC = Json.format[NewDataSourceJDBC]
  implicit val newDataSourceJDBCFormat : Reads[NewDataSourceJDBC] = {
    ((__ \ "jdbcUrl").read[String] and
      (__ \ "driver").read[String] and
      (__ \ "user").read[String] and
      (__ \ "password").read[String] and
      (__ \ "sql").read[String])(NewDataSourceJDBC.apply _)
  }

}