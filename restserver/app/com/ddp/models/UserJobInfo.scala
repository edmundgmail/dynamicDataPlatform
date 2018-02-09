package com.ddp.models

import play.api.libs.json.Json

case class UserJobInfo(name: String, cronTab: String, desc: String)
object UserJobInfo{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def userJobInfoFormat = Json.format[UserJobInfo]
}
