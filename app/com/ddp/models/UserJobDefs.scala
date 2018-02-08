package com.ddp.models

/**
  * Created by eguo on 2/8/18.
  */
object UserJobMessages {
  case object Tick
}

trait UserJobType
case object UserJobTypeEcho extends UserJobType
case object UserJobTypeQueryDB extends UserJobType

trait UserJobInputParams
case class UserJobInputDBParam(dbName: String) extends UserJobInputParams