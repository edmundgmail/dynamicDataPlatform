package com.ddp.dataplatform

import com.ddp.models.{DataSourceType, NewDataSourceJDBC, NewDataSourceRequest, UserJobStatus}
import javax.inject.{Inject, Singleton}

import scala.util.{Failure, Success, Try}
import play.api.db.Databases
import play.api.libs.json._

@Singleton
class DataPlatformConnectionService @Inject()() {
  def testConnection(newDataSourceRequest: NewDataSourceRequest) = {
    DataSourceType.withName(newDataSourceRequest.sType) match {
      case DataSourceType.JDBC =>
        Json.parse(newDataSourceRequest.request).validate[NewDataSourceJDBC].map(jdbc=>
        testJDBC(newDataSourceRequest.name, DataPlatformCoreService.generateUniqueId, jdbc))
          .getOrElse(Failure(new Exception("Parsing Error")))
      case _ =>
    }
  }

  def testJDBC(name: String, uuid: String, jdbc: NewDataSourceJDBC) = {
    val testDb = Databases(
      driver = jdbc.driver,
      url = jdbc.url,
      config = Map(
        "user" -> jdbc.user,
        "password" -> jdbc.pass
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

