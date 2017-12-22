package com.ddp.dataplatform

import javax.inject.Inject

import com.ddp.daos.core.ContextHelper
import com.ddp.daos.exceptions.ServiceException
import com.ddp.models._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import play.api.libs.json.{JsArray, JsObject, JsString, _}
import play.api.mvc._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


class DataPlatformController @Inject()(service: DataPlatformService) extends Controller with ContextHelper {
    private val conf = new SparkConf().setAppName(this.getClass.getCanonicalName).setMaster("local[*]")
    private val spark = SparkSession.builder().config(conf).getOrCreate()

    private def handleException: PartialFunction[Throwable, Result] = {
      case e : ServiceException => BadRequest(e.message)
      case t: Throwable =>   {t.printStackTrace; BadRequest(t.getMessage)}
      case _ => BadRequest("Unknown Exception")
    }


    def create = Action.async(parse.json) {implicit request =>
      validateAndThen[SqlScript] {
        entity => service.createSqlScript(entity).map{
          case Success(e) => Ok(Json.toJson(e))
        }
      } recover handleException
    }

   def get(name: String) = Action.async {
     service.getSqlScript(name).map(
       script => {
         val json = Json.toJson(script)
         Ok(json)
       }
     )
   }

  def validateAndThen[T: Reads](t: T => Future[Result])(implicit request: Request[JsValue]) = {
    request.body.validate[T].map(t) match {
      case JsSuccess(result, _) => result
      case JsError(err) => Future.successful(BadRequest(Json.toJson(err.map {
        case (path, errors) => Json.obj("path" -> path.toString, "errors" -> JsArray(errors.flatMap(e => e.messages.map(JsString(_)))))
      })))
    }
  }

  def toError(t: (String, Try[JsValue])): JsObject = t match {
    case (paramName, Failure(e)) => Json.obj(paramName -> e.getMessage)
    case _ => Json.obj()
  }

 }
