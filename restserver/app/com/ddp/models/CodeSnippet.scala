package com.ddp.models

import com.ddp.daos.core.TemporalModel
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

case class CodeSnippet(name:String, content: String = "",
                       var _id: Option[BSONObjectID] = None,
                       var created: Option[DateTime] = None,
                       var updated: Option[DateTime] = None) extends TemporalModel with UserScript

object CodeSnippet{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def codeSnippetFormat = Json.format[CodeSnippet]
}
