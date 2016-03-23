package br.com.goiaba.utils

import com.typesafe.config.ConfigFactory

/**
  * Created by bruno on 3/23/16.
  */
trait Config {
  private val config = ConfigFactory.load()
  private val inputFileConfig = config.getConfig("loadFile")
  private val httpConfig = config.getConfig("http")

  val filePath = inputFileConfig.getString("path")
  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")
}
