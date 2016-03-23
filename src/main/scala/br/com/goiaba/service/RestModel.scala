package br.com.goiaba.service

import spray.json.DefaultJsonProtocol

/**
  * Created by bruno on 3/21/16.
  */

object RestModel {
  case class Rank(key: Int, score: Double)
  case class Invite(invitee: Int, inviter: Int)

  object ServiceJsonProtocol extends DefaultJsonProtocol {
    implicit val inviteProtocol = jsonFormat2(Invite)
    implicit val rankProtocol = jsonFormat2(Rank)
  }
}