package com.ddp.models

import play.api.libs.json.Json

/**
  * Created by eguo on 2/12/18.
  */
case class UserJobStatus(name: String, language: String, sessionId: String, status: String)
object UserJobStatus{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def userJobStatusFormat = Json.format[UserJobStatus]
}
