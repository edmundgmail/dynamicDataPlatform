package com.ddp.models

import play.api.libs.json.Json

trait NewDataSource {
  val name: String
  val sType: String
}

case class NewDataSourceJDBC(override val name: String, override val sType: String, url: String, driver: String, user: String, pass: String) extends NewDataSource

object NewDataSource{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def newDataSourceJDBC = Json.format[NewDataSourceJDBC]
}
