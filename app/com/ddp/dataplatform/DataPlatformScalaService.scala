package com.ddp.dataplatform

import javax.inject.{Inject, Singleton}

import com.ddp.jarmanager.ScalaSourceCompiler
import com.ddp.models.{ScalaScript, ScriptSimple}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

@Singleton
class DataPlatformScalaService @Inject()(scalaScriptRepository: ScalaScriptRepository) extends DataPlatformCoreService{
  def getScalaScripts : Future[List[ScalaScript]] = {
    scalaScriptRepository.find[ScalaScript]()
  }

  def sparkRun(entity: ScalaScript) = {
    ScalaSourceCompiler.compile(entity)
    val ret = ScalaSourceCompiler.run(entity.name)(spark)
    Success(ret)
  }


  def sparkRunByName(name:String) = {
    getScript(name).flatMap{
      case Some(x) =>
      Future{
        ScalaSourceCompiler.compile(x)
        ScalaSourceCompiler.run(name)(spark)
      }
    }
  }

  def getScript(name: String) :  Future[Option[ScalaScript]] = {
    scalaScriptRepository.findOne(Json.obj("name" -> name))
  }

  def getAllScript : Future[List[ScriptSimple]] = {
    scalaScriptRepository.find[ScriptSimple](projection = Json.obj("name"->1))
  }


  def createOrUpdateScript(entity: ScalaScript) = {
      this.getScript(entity.name).flatMap {
        case Some(script) => scalaScriptRepository.update(script._id.get.stringify, script)
        case _ => scalaScriptRepository.insert(entity)
      }
  }
}

