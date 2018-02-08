package com.ddp.actors

/**
  * Created by eguo on 2/8/18.
  */


import akka.actor._
import java.text.SimpleDateFormat
import java.util.Calendar

import com.ddp.logging.Logging
import com.ddp.models.UserJobInputParams
import com.ddp.models.UserJobMessages.Tick

object UserJobActor {
  def props(params: UserJobInputParams) = Props(new UserJobActor(params))
}

class UserJobActor(param: UserJobInputParams) extends Actor with Logging{
  def receive = {
    case Tick =>
      val timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance.getTime)

      logger.info (s"I received your message at ${timeStamp} ")
  }
}