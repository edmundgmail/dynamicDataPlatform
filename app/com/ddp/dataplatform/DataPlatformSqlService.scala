package com.ddp.dataplatform

import javax.inject.{Inject, Singleton}

import com.ddp.models.{ScriptSimple, SqlScript}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

@Singleton
class DataPlatformSqlService @Inject()(sqlScriptRepository: SqlScriptRepository) extends DataPlatformCoreService{
  def getSqlScripts : Future[List[SqlScript]] = {
    sqlScriptRepository.find[SqlScript]()
  }

  def sparkRun(entity: SqlScript) = {
    val ret = spark.sql(entity.sql).toJSON.collect
    Success(ret)
  }

  def sparkRunByName(name:String) = {
     this.getScript(name).flatMap {
       case Some(x) => {
         Future(spark.sql(x.sql).toJSON.collect)
       }
     }
  }

  def getScript(name: String) :  Future[Option[SqlScript]] = {
    sqlScriptRepository.findOne(Json.obj("name" -> name))
  }

  def getAllScript : Future[List[ScriptSimple]] = {
    sqlScriptRepository.find[ScriptSimple](projection = Json.obj("name"->1))
  }


  def createOrUpdateScript(entity: SqlScript) = {
      this.getScript(entity.name).flatMap {
        case Some(script) => sqlScriptRepository.update(script._id.get.stringify, script)
        case _ => sqlScriptRepository.insert(entity)
      }
  }
}

