package com.ddp.dataplatform

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.ddp.actors.MyWebSocketActor
import com.ddp.daos.core.ContextHelper
import com.ddp.daos.exceptions.ServiceException
import com.ddp.models._
import play.api.libs.json.{JsArray, JsObject, JsString, _}
import play.api.libs.streams.ActorFlow
import play.api.mvc._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class DataPlatformController @Inject()(implicit sqlService: DataPlatformSqlService, scalaService: DataPlatformScalaService, system: ActorSystem, materializer: Materializer) extends Controller with ContextHelper with SameOriginCheck {

    private def handleException: PartialFunction[Throwable, Result] = {
      case e : ServiceException => BadRequest(e.message)
      case t: Throwable =>   {t.printStackTrace; BadRequest(t.getMessage)}
      case _ => BadRequest("Unknown Exception")
    }


    def createOrUpdateSqlScript = Action.async(parse.json) {implicit request =>
      validateAndThen[SqlScript] {
        entity => sqlService.createOrUpdateScript(entity).map{
          case Success(e) => Ok(Json.toJson(e))
        }
      } recover handleException
    }


   def getSqlScript(name: String) = Action.async {
     sqlService.getScript(name).map(
       script => {
         val json = Json.toJson(Some(script))
         Ok(json)
       }
     )
   }

  def sparkRunSQLByName(name: String) =  Action.async {
    sqlService.sparkRunByName(name).map(
      script => {
        val json = Json.toJson(Some(script))
        Ok(json)
      }
    )
  }

  def sparkRunScalaByName(name: String) =  Action.async {
    scalaService.sparkRunByName(name).map(
      script => {
        Ok(script)
      }
    )
  }



  def getAllSqlScript =  Action.async {
    sqlService.getAllScript.map(
      script => {
        val json = Json.toJson(Some(script))
        Ok(json)
      }
    )
  }

  def getAllScalaScript  =  Action.async {
    scalaService.getAllScript.map(
      script => {
        val json = Json.toJson(Some(script))
        Ok(json)
      }
    )
  }

  def ws : WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => MyWebSocketActor.props(out))
  }

  def sparkRunSql = Action.async(parse.json) { implicit request =>
    validateAndThen[SqlScript] {
      entity => {
        Future{
          sqlService.sparkRun(entity) match {
            case Success(e) => Ok(Json.toJson(e))
          }
        }
      }
    } recover handleException
  }

  def sparkRunScala= Action.async(parse.json) { implicit request =>
    validateAndThen[ScalaScript] {
      entity => {
        Future{
          scalaService.sparkRun(entity) match {
            case Success(e) => Ok(e.toString)
          }
        }
      }
    } recover handleException
  }

  def createOrUpdateScalaScript = Action.async(parse.json) {implicit request =>
    validateAndThen[ScalaScript] {
      entity => scalaService.createOrUpdateScript(entity).map{
        case Success(e) => Ok(Json.toJson(e))
      }
    } recover handleException
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

trait SameOriginCheck {

  //def logger: Logger

  /**
    * Checks that the WebSocket comes from the same origin.  This is necessary to protect
    * against Cross-Site WebSocket Hijacking as WebSocket does not implement Same Origin Policy.
    *
    * See https://tools.ietf.org/html/rfc6455#section-1.3 and
    * http://blog.dewhurstsecurity.com/2013/08/30/security-testing-html5-websockets.html
    */
  def sameOriginCheck(rh: RequestHeader): Boolean = {
    rh.headers.get("Origin") match {
      case Some(originValue) if originMatches(originValue) =>
        //logger.debug(s"originCheck: originValue = $originValue")
        true

      case Some(badOrigin) =>
        //logger.error(s"originCheck: rejecting request because Origin header value ${badOrigin} is not in the same origin")
        false

      case None =>
        //logger.error("originCheck: rejecting request because no Origin header found")
        false
    }
  }

  /**
    * Returns true if the value of the Origin header contains an acceptable value.
    *
    * This is probably better done through configuration same as the allowedhosts filter.
    */
  def originMatches(origin: String): Boolean = {
    //origin.contains("localhost:9000") || origin.contains("localhost:19001")
    true
  }

}