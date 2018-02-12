package com.ddp.dataplatform

import javax.inject.{Inject, Singleton}

import com.ddp.jarmanager.ScalaSourceCompiler
import com.ddp.models.{CodeSnippet, ScriptSimple, UserJobFailureStatus, UserJobStatus}
import play.api.Logger
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@Singleton
class DataPlatformScalaService @Inject()(scalaScriptRepository: ScalaScriptRepository) {

  val spark = DataPlatformCoreService.spark

  def getScalaScripts : Future[List[CodeSnippet]] = {
    scalaScriptRepository.find[CodeSnippet]()
  }


  def sparkRun(entity: CodeSnippet) = {
    try{
      val uuid = DataPlatformCoreService.generateUniqueId
      ScalaSourceCompiler.compile(spark, entity)
      ScalaSourceCompiler.run(entity.name)(spark)
       Success(UserJobStatus(entity.name, "scala",  uuid, "Submitted"))
    }
    catch {
      case e => Failure(e)
    }
  }

  def getScript(name: String) :  Future[Option[CodeSnippet]] = {
    scalaScriptRepository.findOne(Json.obj("name" -> name))
  }

  def getAllScript : Future[List[ScriptSimple]] = {
    scalaScriptRepository.find[ScriptSimple](projection = Json.obj("name"->1))
  }


  def createOrUpdateScript(entity: CodeSnippet) = {
    Logger.info("received entity = " + entity.toString)
      this.getScript(entity.name).flatMap {
        case Some(script) => scalaScriptRepository.update(script._id.get.stringify, entity)
        case _ => scalaScriptRepository.insert(entity)
      }
  }
}

