package br.com.goiaba.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.util.Properties

/**
  * Created by bruno on 3/21/16.
  */
class ServerBoot(implicit val system: ActorSystem, implicit  val materializer: ActorMaterializer)
  extends NubankService {
  def startServer(address: String, port: Int) = Http().bindAndHandle(route, address, port)
}

object ServerBoot {
  def main(args: Array[String]) {
    implicit val actorSystem = ActorSystem("nubank-server")
    implicit val materializer = ActorMaterializer()

    val server = new ServerBoot()
    val port = Properties.envOrElse("PORT", "8080").toInt
    val address = Properties.envOrElse("ADDRESS", "localhost")

    server.startServer(address, port)
  }
}
