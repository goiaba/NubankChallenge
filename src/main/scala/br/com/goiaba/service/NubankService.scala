package br.com.goiaba.service

import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import akka.actor.{Props, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import br.com.goiaba.service.RestModel.{Rank, Invite, ServiceJsonProtocol}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._

/**
  * Created by bruno on 3/21/16.
  */
trait NubankService {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

  val persistentActor = system.actorOf(Props[InvitationTreeManager], "tree-persistent-actor")

  import ServiceJsonProtocol._
  val route = pathEndOrSingleSlash {
    getFromResource("public/index.html")
  } ~ path("invitation") {
    post {
      entity(as[Invite]) {
        invite => complete {
          persistentActor ! Invitation(invite)
          s"Invitation from ${invite.inviter} to ${invite.invitee} will be treated by the server."
        }
      }
    }
  } ~ path("ranking") {
    get {
      complete {
        import spray.json._
        import DefaultJsonProtocol._

        implicit val timeout = Timeout(2.second)

        val fut: Future[List[(Int, Double)]] = ask(persistentActor, Ranking).mapTo[List[(Int, Double)]]
        Await.result(fut, 5.second).map(x => Rank(x._1, x._2)).toJson
      }
    }
  }
}
