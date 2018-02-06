package com.ddp.connectors

import com.ddp.dataplatform.DataPlatformCoreService
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.{ReadConfig, WriteConfig}

/**
  * Created by eguo on 2/2/18.
  */
class MongoConnector(url: String) {

}

case class UserMovieRating(name: String, rate: Int)

object MongoConnector{
  def main(args: Array[String]): Unit ={
    val sc = DataPlatformCoreService.spark.sparkContext

    val readConfig = ReadConfig(Map("uri" -> "mongodb://127.0.0.1/ddp.movie_ratings?readPreference=primaryPreferred"))
    val writeConfig = WriteConfig(Map("uri" -> "mongodb://127.0.0.1/ddp.user_recommendations"))

    val userId = 0

    // Load the movie rating data
    val movieRatings = MongoSpark.load(sc, readConfig).toDF[UserMovieRating]

    println("hello")
  }
}