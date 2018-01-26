package com.ddp.models

import com.ddp.daos.core.TemporalModel
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

case class ScalaScript(name:String, text: String = "",
                     var _id: Option[BSONObjectID] = None,
                     var created: Option[DateTime] = None,
                     var updated: Option[DateTime] = None) extends TemporalModel with UserScript

object ScalaScript{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def scalaScriptFormat = Json.format[ScalaScript]
}
