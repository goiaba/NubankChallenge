package br.com.goiaba.service

import akka.actor.Actor
import akka.event.Logging
import br.com.goiaba.core.model.InvitationTree
import br.com.goiaba.service.RestModel.Invite
import br.com.goiaba.utils.InitialLoadManager

import scala.util.{Failure, Success, Try}

/**
  * Created by bruno on 3/22/16.
  */

sealed trait Request
case class Invitation(invite: Invite) extends Request
case object Ranking extends Request
case object ShutDown extends Request

class InvitationTreeManager extends Actor {
  val log = Logging(context.system, this)
  protected var state: Option[InvitationTree] = InitialLoadManager.loadInvitationsFromFile

  def ranking: List[(Int, Double)] = state match {
    case Some(tree) => tree.ranking
    case None => Nil
  }

  def updateState(i: Invite): Option[InvitationTree] = state match {
    case Some(tree) => {
      Try(tree.insert(i.invitee, i.inviter)) match {
        case Success(v) => Some(v)
        case Failure(e) => {
          log.info(
            s"Invitation from ${i.inviter} to ${i.invitee} was ignored due to the following error: ${e.getMessage}")
          state
        }
      }
    }
    case None => Some(InvitationTree(i.inviter).insert(i.invitee, i.inviter))
  }

  override def receive: Receive = {
    case Invitation(invite) => state = {
      log.info(s"Invitation request received (${invite.inviter} inviting ${invite.invitee}")
      updateState(invite)
    }
    case Ranking => {
      log.info("Ranking request received")
      sender ! ranking
    }
    case ShutDown => {
      log.info("Shutdown request received")
      context.system.terminate()
    }
  }
}