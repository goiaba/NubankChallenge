package br.com.goiaba.service

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by bruno on 3/22/16.
  */
class NubankServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with NubankService {

  "In Nubank API, GETting from /" should {
    "results in an html page" in {
      val getRequest = HttpRequest(HttpMethods.GET, uri = "/")
      getRequest ~> route ~> check {
        status.isSuccess() shouldEqual true
        responseAs[String].contains("Welcome to nubank invitation challenge service.") shouldEqual true
      }
    }
  }

  "In Nubank API, GETting from /ranking after initial file load" should {
    "results in a non empty list of scores" in {
      val getRequest = HttpRequest(HttpMethods.GET, uri = "/ranking")
      getRequest ~> route ~> check {
        status.isSuccess() shouldEqual true
        val resp = responseAs[String]
        resp.startsWith("[{") shouldEqual true
        resp.contains("key") shouldEqual true
        resp.contains("score") shouldEqual true
        resp.contains("}]") shouldEqual true
      }
    }
  }
  "In Nubank API, POSTing to /invitation" should {
    "results in a confirmation answer" in {
      val data = ByteString(
        s"""
           |{
           |    "invitee":2, "inviter": 1
           |}
        """.stripMargin)
      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/invitation",
        entity = HttpEntity(MediaTypes.`application/json`, data))

      postRequest ~> route ~> check {
        responseAs[String] shouldEqual "Invitation from 1 to 2 will be treated by the server."
      }
    }
  }
}
