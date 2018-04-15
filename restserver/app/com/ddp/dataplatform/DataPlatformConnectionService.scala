package com.ddp.dataplatform

import com.ddp.models.{NewDataSourceJDBC, UserJobStatus}
import javax.inject.{Inject, Singleton}

import scala.util.Success
import play.api.db.Databases

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class DataPlatformConnectionService @Inject()() {
  def testJDBCConnection(newDataSourceJDBC: NewDataSourceJDBC) = {
    val uuid = DataPlatformCoreService.generateUniqueId

    val testDb = Databases(
      driver = newDataSourceJDBC.driver,
      url = newDataSourceJDBC.url,
      name = "ddp",
      config = Map(
        "user" -> "root",
        "password" -> "password"
      )
    )
    val conn = testDb.getConnection()
    Success(UserJobStatus(newDataSourceJDBC.name, "testConnection", uuid, "Success"))
  }
}

