package com.ddp.dataplatform

import javax.inject.Inject

import com.ddp.daos.core.ContextHelper
import com.ddp.daos.exceptions.ServiceException
import com.ddp.models._
import org.apache.hadoop.fs.Path
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import play.api.libs.json.{JsArray, JsObject, JsString, _}
import play.api.mvc._

import scala.collection.mutable
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


class DataPlatformController @Inject()(service: DataPlatformService) extends Controller with ContextHelper {
    private val conf = new SparkConf().setAppName(this.getClass.getCanonicalName).setMaster("local[*]")
    private val spark = SparkSession.builder().config(conf).getOrCreate()
    @transient private var userScripts: mutable.Map[String, String] = mutable.HashMap.empty

    private def handleException: PartialFunction[Throwable, Result] = {
      case e : ServiceException => BadRequest(e.message)
      case t: Throwable =>   {t.printStackTrace; BadRequest(t.getMessage)}
      case _ => BadRequest("Unknown Exception")
    }

    def sp_create(name:String, script: String) = {
        userScripts += name -> script
        Ok("created")
    }

   def sp(name: String) : Any = {
     userScripts.get(name).flatMap(s=>Some(spark.sql(s)))
   }

 }
