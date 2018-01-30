### Features
* Mongo DB Migration support to organize DB changes
* JSON Fixtures support for inserting fixture data for integration test
* Mongo DB integration test

POST: http://localhost:9000/sp/runScala
{
	"name":"com.ddp.user.MyClass3",
	"text":"package com.ddp.user\n import org.apache.spark.sql.SparkSession\n class MyClass3 (spark:SparkSession){  def run = {spark.sql(\"show tables\")}} "
}