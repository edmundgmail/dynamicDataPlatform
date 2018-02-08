package com.ddp.actors

/**
  * Created by eguo on 2/8/18.
  */


import akka.actor._
import java.text.SimpleDateFormat
import java.util.Calendar

import com.ddp.actors.UserJobActor.SayHello
import com.ddp.logging.Logging
import com.ddp.models.UserJobInputParams
import com.ddp.models.UserJobMessages.Tick
import play.api.Logger

object UserJobActor {
  def props(params: UserJobInputParams) = Props(new UserJobActor(params))
  case class SayHello(name: String)

}

class UserJobActor(param: UserJobInputParams) extends Actor{
  val logger = Logger.logger
  def receive = {
    case SayHello(name) =>
      val timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance.getTime)

      println(s"hello, world at ${timeStamp}")
      logger.info(s"I received your message at ${timeStamp} ")
  }
}