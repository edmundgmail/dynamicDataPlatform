package com.ddp.models

import com.ddp.daos.core.TemporalModel
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

case class SqlScript(name:String, sql: String,
                     var _id: Option[BSONObjectID] = None,
                     var created: Option[DateTime] = None,
                     var updated: Option[DateTime] = None) extends TemporalModel

object SqlScript{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def sqlScriptFormat = Json.format[SqlScript]

}
