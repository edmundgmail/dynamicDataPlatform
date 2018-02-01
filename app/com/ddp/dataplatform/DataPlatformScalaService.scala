package com.ddp.dataplatform

import javax.inject.{Inject, Singleton}

import com.ddp.jarmanager.ScalaSourceCompiler
import com.ddp.models.{CodeSnippet, ScriptSimple}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

@Singleton
class DataPlatformScalaService @Inject()(scalaScriptRepository: ScalaScriptRepository) extends DataPlatformCoreService{
  def getScalaScripts : Future[List[CodeSnippet]] = {
    scalaScriptRepository.find[CodeSnippet]()
  }

  def sparkRun(entity: CodeSnippet) = {
    ScalaSourceCompiler.compile(entity)
    val ret = ScalaSourceCompiler.run(entity.name)(spark)
    Success(ret)
  }



  def getScript(name: String) :  Future[Option[CodeSnippet]] = {
    scalaScriptRepository.findOne(Json.obj("name" -> name))
  }

  def getAllScript : Future[List[ScriptSimple]] = {
    scalaScriptRepository.find[ScriptSimple](projection = Json.obj("name"->1))
  }


  def createOrUpdateScript(entity: CodeSnippet) = {
      this.getScript(entity.name).flatMap {
        case Some(script) => scalaScriptRepository.update(script._id.get.stringify, script)
        case _ => scalaScriptRepository.insert(entity)
      }
  }
}

