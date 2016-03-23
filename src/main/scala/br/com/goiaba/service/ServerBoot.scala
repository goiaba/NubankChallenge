package br.com.goiaba.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import br.com.goiaba.utils.Config

/**
  * Created by bruno on 3/21/16.
  */
class ServerBoot(implicit val system: ActorSystem, implicit  val materializer: ActorMaterializer)
  extends NubankService with Config {
  def startServer() = Http().bindAndHandle(route, httpInterface, httpPort)
}

object ServerBoot {
  def main(args: Array[String]) {
    implicit val actorSystem = ActorSystem("nubank-server")
    implicit val materializer = ActorMaterializer()

    val server = new ServerBoot()
    server.startServer()
  }
}
