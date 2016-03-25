package br.com.goiaba.model

import br.com.goiaba.core.model.InvitationTree
import org.scalatest.FunSuite

/**
  * Created by bruno on 3/13/16.
  */
class InvitationTreeSpec extends FunSuite {

  val completeTree = InvitationTree(1)
    .insert(2, 1)
    .insert(3, 1)
    .insert(4, 3)
    .insert(4, 2)
    .insert(5, 4)
    .insert(6, 4)
    .insert(7, 5)
    .insert(7, 6)
    .insert(8, 6)

  test("Nonexistent key inviting should throw an exception") {
    intercept[RuntimeException] {
      assert(Fixtures.initialTree == InvitationTree(1)
        .insert(2, 4))
    }
  }

  test("One invites Two") {
    assert(Fixtures.oneInvitesTwo == InvitationTree(1)
      .insert(2, 1))
  }

  test("One invites Three") {
    assert(Fixtures.oneInvitesThree == InvitationTree(1)
      .insert(2, 1)
      .insert(3, 1))
  }

  test("Three invites Four") {
    assert(Fixtures.threeInvitesFour == InvitationTree(1)
      .insert(2, 1)
      .insert(3, 1)
      .insert(4, 3))
  }

  test("Two invites Four") {
    assert(Fixtures.twoInvitesFour == InvitationTree(1)
			.insert(2, 1)
			.insert(3, 1)
			.insert(4, 3)
			.insert(4, 2))
  }

  test("Four invites Five") {
    assert(Fixtures.fourInvitesFive == InvitationTree(1)
			.insert(2, 1)
			.insert(3, 1)
			.insert(4, 3)
			.insert(4, 2)
			.insert(5, 4))
  }

  test("Four invites Six") {
    assert(Fixtures.fourInvitesSix == InvitationTree(1)
			.insert(2, 1)
			.insert(3, 1)
			.insert(4, 3)
			.insert(4, 2)
			.insert(5, 4)
			.insert(6, 4))
  }

  test("Five invites Seven") {
    assert(Fixtures.fiveInvitesSeven == InvitationTree(1)
			.insert(2, 1)
			.insert(3, 1)
			.insert(4, 3)
			.insert(4, 2)
			.insert(5, 4)
			.insert(6, 4)
			.insert(7, 5))
  }

  test("Six invites Seven") {
    assert(Fixtures.sixInvitesSeven == InvitationTree(1)
			.insert(2, 1)
			.insert(3, 1)
			.insert(4, 3)
			.insert(4, 2)
			.insert(5, 4)
			.insert(6, 4)
			.insert(7, 5)
			.insert(7, 6))
  }

  test("Six invites Eight") {
    assert(Fixtures.sixInvitesEight == completeTree)
  }

  test("Verify pathTo One") {
    assert (Fixtures.pathToOne == completeTree.pathTo(1).map(_.key))
  }

  test("Verify pathTo Four") {
    assert (Fixtures.pathToFour == completeTree.pathTo(4).map(_.key))
  }

  test("Verify pathTo Eight") {
    assert (Fixtures.pathToEight == completeTree.pathTo(8).map(_.key))
  }

  test("Verify find One") {
    assert (completeTree.find(1).get.key == 1)
  }

  test("Verify find Five") {
    assert (completeTree.find(5).get.key == 5)
  }

  test("Verify find Six") {
    assert (completeTree.find(6).get.key == 6)
  }

  test("Score list before invitations") {
    val userByScore = InvitationTree(1).ranking
    assert(userByScore.isEmpty)
  }

  test("Verify pathTo One") {
    assert (Fixtures.pathToOne == InvitationTree(1)
      .insert(2, 1)
      .insert(3, 1)
      .insert(4, 3)
      .insert(4, 2)
      .insert(5, 4)
      .insert(6, 4)
      .insert(7, 5)
      .insert(7, 6)
      .insert(8, 6)
      .pathTo(1).map(_.key))
  }

  test("Verify pathTo Four") {
    assert (Fixtures.pathToFour == InvitationTree(1)
      .insert(2, 1)
      .insert(3, 1)
      .insert(4, 3)
      .insert(4, 2)
      .insert(5, 4)
      .insert(6, 4)
      .insert(7, 5)
      .insert(7, 6)
      .insert(8, 6)
      .pathTo(4).map(_.key))
  }

  test("Verify pathTo Eight") {
    assert (Fixtures.pathToEight == InvitationTree(1)
      .insert(2, 1)
      .insert(3, 1)
      .insert(4, 3)
      .insert(4, 2)
      .insert(5, 4)
      .insert(6, 4)
      .insert(7, 5)
      .insert(7, 6)
      .insert(8, 6)
      .pathTo(8).map(_.key))
  }

  test("Verify find One") {
    val treeOne = InvitationTree(1)
      .insert(2, 1)
      .insert(3, 1)
      .insert(4, 3)
      .insert(4, 2)
      .insert(5, 4)
      .insert(6, 4)
      .insert(7, 5)
      .insert(7, 6)
      .insert(8, 6)
      .find(1)

    assert (treeOne.get.key == 1)
  }

  test("Verify find Five") {
    val treeOne = InvitationTree(1)
      .insert(2, 1)
      .insert(3, 1)
      .insert(4, 3)
      .insert(4, 2)
      .insert(5, 4)
      .insert(6, 4)
      .insert(7, 5)
      .insert(7, 6)
      .insert(8, 6)
      .find(5)

    assert (treeOne.get.key == 5)
  }

  test("Verify find Six") {
    val treeOne = InvitationTree(1)
      .insert(2, 1)
      .insert(3, 1)
      .insert(4, 3)
      .insert(4, 2)
      .insert(5, 4)
      .insert(6, 4)
      .insert(7, 5)
      .insert(7, 6)
      .insert(8, 6)
      .find(6)

    assert (treeOne.get.key == 6)
  }

  test("Score list after invitations") {
    val userByScore = completeTree.ranking

    //Seven and Eight didn't invite anyone.
    assert(userByScore.size == 6)
    //One has the bigger score
    assert(userByScore.head == (1, 3.0))
    //Three and Four have the same score, less than One's score.
    assert(userByScore.indexOf((3, 2.0)) > userByScore.indexOf((1, 3.0)))
    assert(userByScore.indexOf((4, 2.0)) > userByScore.indexOf((1, 3.0)))
    //Five and Six have score zero. They must be the last ones in the list
    assert(userByScore.indexOf((5, 0.0)) > userByScore.indexOf((4, 2.0)))
    assert(userByScore.indexOf((6, 0.0)) > userByScore.indexOf((4, 2.0)))
  }

}
