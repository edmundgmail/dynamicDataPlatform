package com.ddp.dataplatform

import javax.inject.{Inject, Singleton}

import com.ddp.models.SqlScript
import org.apache.spark.sql.SparkSession
import play.api.Logger
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.util.Try

@Singleton
class DataPlatformService @Inject()(sqlScriptRepository: SqlScriptRepository) {

  def getSqlScripts : Future[List[SqlScript]] = {
    sqlScriptRepository.find[SqlScript]()
  }


  def getSqlScript(name: String) :  Future[Option[SqlScript]] = {
    sqlScriptRepository.findOne(Json.obj("name" -> name))
  }


  def createSqlScript(entity: SqlScript) = {
      this.getSqlScript(entity.name).flatMap {
        case Some(script) => sqlScriptRepository.update(script._id.get.stringify, script)
        case _ => sqlScriptRepository.insert(entity)
      }
  }


}

