package com.ddp.jarmanager

import java.io._
import java.lang.reflect.Method

import com.ddp.models.{ScalaScript, SqlScript}
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
  @transient private var clazzExModule: mutable.Map[String, Class[_]] = mutable.HashMap.empty

  def compile(sources: ScalaScript):Unit = {

    val  targetDir = new File("target_" + System.currentTimeMillis + "_" + Random.nextInt(10000) + ".tmp")

    targetDir.mkdir

    val eval = new Eval(Some(targetDir))

    eval.compile(sources.text)

    val jarFile = CreateJarFile.mkJar(targetDir, "Main")
    loadJar(sources.name, jarFile)

    FileUtils.forceDelete(targetDir)
    FileUtils.forceDelete(new File(jarFile))
  }

  private def loadJar(name: String, jarFile: String) = {
    urlClassLoader.addURL((new File(jarFile)).toURI.toURL)
    clazzExModule += name->urlClassLoader.loadClass(name)
  }

  def run(name:String, func: String = "run")(implicit spark:SparkSession) : JsObject = {
    val classLoader=  clazzExModule.get(name).get
    val instance = classLoader.getConstructor(classOf[SparkSession]).newInstance(spark)
    val method: Method = classLoader.getDeclaredMethod(func)
    method.invoke(instance).asInstanceOf[JsObject]
  }

}
