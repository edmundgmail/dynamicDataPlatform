package com.ddp.dataplatform

import javax.inject.Inject

import com.ddp.daos.core.{DocumentDao, Repository}
import com.ddp.models.SqlScript
import com.google.inject.Singleton
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType

import scala.concurrent.Future

@Singleton
class SqlScriptRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends DocumentDao[SqlScript](reactiveMongoApi) with Repository[SqlScript]  {

  override def collectionName = "sqlScriptTable"

  override def ensureIndexes: Future[Boolean] = ensureIndex(List("name" -> IndexType.Ascending), unique = true)

  ensureIndexes
}
