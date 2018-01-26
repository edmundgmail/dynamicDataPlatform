package com.ddp.models

import play.api.libs.json.Json

case class ScriptSimple(name:String)
object ScriptSimple{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def scriptSimpleFormat = Json.format[ScriptSimple]
}