package com.ddp.utils

import java.net.{URL, URLClassLoader}

import org.slf4j.LoggerFactory

/**
  * Created by eguo on 2/5/18.
  */
class ContextURLClassLoader(urls: Array[URL], parent: ClassLoader)
  extends URLClassLoader(urls, parent) {
  val logger = LoggerFactory.getLogger(getClass)

  def hasUrl(url: URL) : Boolean = getURLs.contains(url)

  override def addURL(url: URL) {
    super.addURL(url)
    logger.info("Added URL " + url + " to ContextURLClassLoader")
  }
}
