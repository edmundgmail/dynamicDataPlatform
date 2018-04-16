package com.ddp.dataplatform

import com.ddp.models.{DataSourceType, NewDataSourceJDBC, NewDataSourceRequest, UserJobStatus}
import javax.inject.{Inject, Singleton}

import scala.util.Success
import play.api.db.Databases
import play.libs.Json

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class DataPlatformConnectionService @Inject()() {
  def testConnection(newDataSourceRequest: NewDataSourceRequest) = {
    val uuid = DataPlatformCoreService.generateUniqueId

    if(DataSourceType.withName(newDataSourceRequest.sType) == DataSourceType.JDBC)
    {

      val jdbc = Json.parse(newDataSourceRequest.request).asInstanceOf[NewDataSourceJDBC]

      val testDb = Databases(
        driver = jdbc.driver,
        url = jdbc.url,
        name = newDataSourceRequest.name,
        config = Map(
          "user" -> jdbc.user,
          "password" -> jdbc.pass
        )
      )
      val conn = testDb.getConnection()
      try {
        conn.createStatement.execute(jdbc.sql)
        Success(UserJobStatus(newDataSourceRequest.name, "testConnection", uuid, "Success"))
      } finally {
        conn.close()
      }
    }
  }
}

