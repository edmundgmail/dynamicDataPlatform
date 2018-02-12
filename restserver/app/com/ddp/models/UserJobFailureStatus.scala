package com.ddp.models

import play.api.libs.json.Json

/**
  * Created by eguo on 2/12/18.
  */
case class UserJobFailureStatus(name: String, language: String,  errorMessage: String, cause: String)
object UserJobFailureStatus{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def userJobFailureStatusFormat = Json.format[UserJobFailureStatus]
}
