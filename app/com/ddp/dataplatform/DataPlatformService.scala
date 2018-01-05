package com.ddp.dataplatform

import javax.inject.{Inject, Singleton}

import com.ddp.models.SqlScript
import org.apache.spark.SparkConf
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class DataPlatformService @Inject()(sqlScriptRepository: SqlScriptRepository) {

  private val conf = new SparkConf().setAppName(this.getClass.getCanonicalName).setMaster("local[*]")
  private val spark = SparkSession.builder().config(conf).getOrCreate()

  def getSqlScripts : Future[List[SqlScript]] = {
    sqlScriptRepository.find[SqlScript]()
  }


  def sparkRun(sql: Option[SqlScript])  : Future[List[SqlScript]] = ???  /*{
    sql.flatMap {
      case Some(s) => Some(spark.sql(s).toJSON)
      case _ => None
    }
  }*/

  def getSqlScript(name: String) :  Future[Option[SqlScript]] = {
    sqlScriptRepository.findOne(Json.obj("name" -> name))
  }


  def createOrUpdateSqlScript(entity: SqlScript) = {
      this.getSqlScript(entity.name).flatMap {
        case Some(script) => sqlScriptRepository.update(script._id.get.stringify, script)
        case _ => sqlScriptRepository.insert(entity)
      }
  }


}

