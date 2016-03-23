package br.com.goiaba.service

import akka.actor.Actor
import akka.event.Logging
import br.com.goiaba.core.model.InvitationTree
import br.com.goiaba.service.RestModel.Invite

import scala.io.Source
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
  protected var state: Option[InvitationTree] = loadInvitationsFromFile

  /**
    *
    * @return the initial InvitationTree that is populated with the
    *         data provided by a text file. Each line of this file
    *         must follow the pattern "key1\skey2" where key1
    *         represents the inviter and key2 represents the
    *         invited user.
    *
    */
  private def loadInvitationsFromFile: Option[InvitationTree] = {
    val filename = "../../../../input.txt"

    Try(Source.fromURL(getClass.getResource(filename))) match {
      case Success(v) => {
        print("Reading the input data file... ")
        val iterator = v.getLines()
        val (root, invitee) = transformLine(iterator.next())
        val tree = Option(iterator.foldLeft(InvitationTree(root).insert(invitee, root))((acc, item) => {
          val f = transformLine(item)
          acc.insert(f._2, f._1)
        }))
        println("done")
        tree
      }
      case Failure(e) => println(s"Error reading input file: ${e.getMessage}"); None
    }
  }

  /**
    * This method will throw a RuntimeException if the keys are
    *  not of type Integer or if the pattern found in the read
    *  line is not the expected.
    *
    * @param line Represents a pair of user keys
    * @return a tuple containing both keys
    */
  private def transformLine(line: String): (Int, Int) = {
    val splitRegex = """\s+"""
    line.split(splitRegex) match {
      case Array(f1, f2) => Try((f1.toInt, f2.toInt)).getOrElse(
        throw new RuntimeException("Keys in the input file must be of Integer type"))
      case _ => throw new RuntimeException("Input file does not follow pattern \"key1 key2\\n\"")
    }
  }

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