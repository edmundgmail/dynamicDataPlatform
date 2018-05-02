package com.ddp.dataplatform

import com.ddp.connectors.JDBCSparkConnector
import com.ddp.models._
import javax.inject.{Inject, Singleton}
import org.apache.spark.sql.DataFrame

import scala.util.{Failure, Success, Try}
import play.api.db.Databases
import play.api.libs.json._

@Singleton
class DataPlatformConnectionService @Inject()() {
  def testConnection(newDataSourceRequest: NewDataSourceRequest) = {
    DataSourceType.withName(newDataSourceRequest.sType) match {
      case DataSourceType.JDBC =>
        Json.parse(newDataSourceRequest.request).validate[NewDataSourceJDBC].map(jdbc => {
          //createRDDFromJDBC(jdbc)
          testJDBC(newDataSourceRequest.name, DataPlatformCoreService.generateUniqueId, jdbc)
        })
          .getOrElse(Failure(new Exception("Parsing Error")))

      /*case DataSourceType.HBASE =>
        Json.parse(newDataSourceRequest.request).validate[NewDataSourceHBase].map(hbase=>{
          testHBase(newDataSourceRequest.name, DataPlatformCoreService.generateUniqueId, hbase)
        }).getOrElse(Failure(new Exception("Parsing error")))*/
      case _ =>
    }
  }


  def createRDDFromJDBC(jdbc: NewDataSourceJDBC) : DataFrame = {
    val conn = JDBCSparkConnector(DataPlatformCoreService.spark, jdbc)
    conn.df
  }

  def testJDBC(name: String, uuid: String, jdbc: NewDataSourceJDBC) = {
    val testDb = Databases(
      driver = jdbc.driver,
      url = jdbc.jdbcUrl,
      config = Map(
        "user" -> jdbc.user,
        "password" -> jdbc.password
      )
    )

    try{
      testDb.withConnection(
        conn => {
          conn.createStatement.execute(jdbc.sql)
          Success(UserJobStatus(name, "testConnection", uuid, "Success"))
        })
    }
    catch {
      case e : Throwable => Failure(e)
    }
  }
}

