package br.com.goiaba.utils

import br.com.goiaba.core.model.InvitationTree

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * Created by bruno on 3/23/16.
  */
object InitialLoadManager extends Config {
  /**
    *
    * @return the initial InvitationTree that is populated with the
    *         data provided by a text file. Each line of this file
    *         must follow the pattern "key1\skey2" where key1
    *         represents the inviter and key2 represents the
    *         invited user.
    *
    */
  def loadInvitationsFromFile: Option[InvitationTree] = {
    Try(Source.fromURL(getClass.getResource(filePath))) match {
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
}
