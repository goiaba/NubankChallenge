package br.com.goiaba.model

import br.com.goiaba.core.model.InvitationTree
import org.scalatest.FunSuite

/**
  * Created by bruno on 3/13/16.
  */
class InvitationTreeSpec extends FunSuite {

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
    assert(Fixtures.sixInvitesEight == InvitationTree(1)
			.insert(2, 1)
			.insert(3, 1)
			.insert(4, 3)
			.insert(4, 2)
			.insert(5, 4)
			.insert(6, 4)
			.insert(7, 5)
			.insert(7, 6)
			.insert(8, 6))
  }

	test("Score list before invitation invitations") {
		val userByScore = InvitationTree(1)
			.ranking

		assert(userByScore.isEmpty)
	}

  test("Score list after invitations") {
    val userByScore = InvitationTree(1)
      .insert(2, 1)
      .insert(3, 1)
      .insert(4, 3)
      .insert(4, 2)
      .insert(5, 4)
      .insert(6, 4)
      .insert(7, 5)
      .insert(7, 6)
      .insert(8, 6)
      .ranking

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
