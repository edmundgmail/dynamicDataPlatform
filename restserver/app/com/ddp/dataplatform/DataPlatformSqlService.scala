package com.ddp.dataplatform

import javax.inject.{Inject, Singleton}

import com.ddp.models.{ScriptSimple, CodeSnippet}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

@Singleton
class DataPlatformSqlService @Inject()(sqlScriptRepository: SqlScriptRepository){
  val spark = DataPlatformCoreService.spark

  def getSqlScripts : Future[List[CodeSnippet]] = {
    sqlScriptRepository.find[CodeSnippet]()
  }

  def sparkRun(entity: CodeSnippet) = {
    val ret = spark.sql(entity.content).toJSON.collect
    Success(ret)
  }

  def sparkRunByName(name:String) = {
     this.getScript(name).flatMap {
       case Some(x) => {
         Future(spark.sql(x.content).toJSON.collect)
       }
     }
  }

  def getScript(name: String) :  Future[Option[CodeSnippet]] = {
    sqlScriptRepository.findOne(Json.obj("name" -> name))
  }

  def getAllScript : Future[List[ScriptSimple]] = {
    sqlScriptRepository.find[ScriptSimple](projection = Json.obj("name"->1))
  }


  def createOrUpdateScript(entity: CodeSnippet) = {
      this.getScript(entity.name).flatMap {
        case Some(script) => sqlScriptRepository.update(script._id.get.stringify, script)
        case _ => sqlScriptRepository.insert(entity)
      }
  }
}

