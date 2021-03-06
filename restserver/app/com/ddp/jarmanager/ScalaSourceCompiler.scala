package com.ddp.jarmanager

import java.io._
import java.net.URL

import com.ddp.models.CodeSnippet
import com.ddp.userapi.SparkJobApi
import com.ddp.utils.ContextURLClassLoader
import com.twitter.util.Eval
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.SparkSession

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.Logger
/**
  * Created by cloudera on 9/3/16.
*/

object ScalaSourceCompiler {

  private val jarLoader = new ContextURLClassLoader(Array[URL](), getClass.getClassLoader)

  def compile(spark: SparkSession, sources: CodeSnippet) = {
    val jarName : String = s"${sources.name}.jar"
    val url = new File(jarName).toURI.toURL

    if(jarLoader.hasUrl(url)){
        //throw new Exception(s"${sources.name} already existed. Please change the name of class or package")
    }

    val  targetDir = new File(s"${sources.name}")

    targetDir.mkdir

    val eval = new Eval(Some(targetDir))

    eval.compile(sources.content)

    val jarFile = CreateJarFile.mkJar(targetDir, sources.name)
    spark.sparkContext.addJar(jarFile)
    jarLoader.addURL(url)

    FileUtils.forceDelete(targetDir)
    //FileUtils.forceDelete(new File(jarFile))
  }


  def run(name:String)(implicit spark:SparkSession) = {
    //val instance = classLoader.getConstructor(classOf[SparkSession]).newInstance(spark)
   // val method: Method = classLoader.getDeclaredMethod(func)
    //method.invoke(instance)
    try{

      Thread.currentThread.setContextClassLoader(jarLoader)
      val instance = jarLoader.loadClass(name).newInstance.asInstanceOf[SparkJobApi]
      val ret = instance.runJob(spark)
      Logger.info(s"ret=${ret}")
      ret
    }
    catch {
      case e:Throwable => Logger.info(s"${e.getMessage} ${e.getCause}")
    }
  }

}
