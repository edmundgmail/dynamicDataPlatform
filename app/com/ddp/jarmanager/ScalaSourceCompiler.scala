package com.ddp.jarmanager

import java.io._
import java.lang.reflect.Method

import com.ddp.models.{CodeSnippet}
import com.twitter.util.Eval
import org.apache.commons.io.{FileUtils, IOUtils}
import org.apache.hadoop
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession
import play.api.libs.json.{JsObject, JsValue}

import scala.collection.mutable
import scala.reflect.internal.util.ScalaClassLoader.URLClassLoader
import scala.util.Random
/**
  * Created by cloudera on 9/3/16.
  */


object ScalaSourceCompiler {

  private val urlClassLoader = new URLClassLoader(Seq.empty, this.getClass.getClassLoader)
  private var clazzExModule: mutable.Map[String, Class[_]] = mutable.HashMap.empty

  def compile(spark: SparkSession, sources: CodeSnippet) = {

    if(clazzExModule contains sources.name){
        throw new Exception(s"${sources.name} already existed. Please change the name of class or package")
    }

    val  targetDir = new File(s"${sources.name}")

    targetDir.mkdir

    val eval = new Eval(Some(targetDir))

    eval.compile(sources.content)

    val jarFile = CreateJarFile.mkJar(targetDir, sources.name)
    loadJar(spark, sources.name, jarFile)

    //FileUtils.forceDelete(targetDir)
    //FileUtils.forceDelete(new File(jarFile))
  }

  private def loadJar(spark: SparkSession, name: String, jarFile: String) = {
    //urlClassLoader.addURL((new File(jarFile)).toURI.toURL)
    clazzExModule += name-> ScalaSourceCompiler.getClass /*urlClassLoader.loadClass(name)*/
    spark.sparkContext.addJar(jarFile)
  }

  def run(name:String, func: String = "run")(implicit spark:SparkSession) : Any = {
    //val classLoader=  clazzExModule.get(name).get
    //val instance = classLoader.getConstructor(classOf[SparkSession]).newInstance(spark)
   // val method: Method = classLoader.getDeclaredMethod(func)
    //method.invoke(instance)
  }

}
