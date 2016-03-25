package br.com.goiaba.model

import br.com.goiaba.core.model.{InvitationTree, Data}


/**
  * Created by bruno on 3/12/16.
  */
object Fixtures {

  val initialTree = InvitationTree(1)

  val pathToOne = List(1)

  val pathToFour = List(4, 3, 1)

  val pathToEight = List(8, 6, 4, 3, 1)

  val oneInvitesTwo =
    InvitationTree(1, Data(hasInvitedBefore = true), Set(
      InvitationTree(2, Data())
    ))

  val oneInvitesThree =
    InvitationTree(1, Data(hasInvitedBefore = true), Set(
      InvitationTree(2, Data()),
      InvitationTree(3, Data())
    ))

  val threeInvitesFour =
    InvitationTree(1, Data(1.0, hasInvitedBefore = true), Set(
      InvitationTree(2, Data()),
      InvitationTree(3, Data(hasInvitedBefore = true), Set(
        InvitationTree(4, Data())
      ))
    ))

  val twoInvitesFour =
    InvitationTree(1, Data(2.0, hasInvitedBefore = true), Set(
      InvitationTree(2, Data(hasInvitedBefore = true)),
      InvitationTree(3, Data(hasInvitedBefore = true), Set(
        InvitationTree(4, Data())
      ))
    ))

  val fourInvitesFive =
    InvitationTree(1, Data(2.5, hasInvitedBefore = true), Set(
      InvitationTree(2, Data(hasInvitedBefore = true)),
      InvitationTree(3, Data(1.0, hasInvitedBefore = true), Set(
        InvitationTree(4, Data(hasInvitedBefore = true), Set(
          InvitationTree(5, Data())
        ))
      ))
    ))

  val fourInvitesSix =
    InvitationTree(1, Data(2.5, hasInvitedBefore = true), Set(
      InvitationTree(2, Data(hasInvitedBefore = true)),
      InvitationTree(3, Data(1.0, hasInvitedBefore = true), Set(
        InvitationTree(4, Data(hasInvitedBefore = true), Set(
          InvitationTree(5, Data()),
          InvitationTree(6, Data())
        ))
      ))
    ))

  val fiveInvitesSeven =
    InvitationTree(1, Data(2.75, hasInvitedBefore = true), Set(
      InvitationTree(2, Data(hasInvitedBefore = true)),
      InvitationTree(3, Data(1.5, hasInvitedBefore = true), Set(
        InvitationTree(4, Data(1.0, hasInvitedBefore = true), Set(
          InvitationTree(5, Data(hasInvitedBefore = true), Set(
            InvitationTree(7, Data())
          )),
          InvitationTree(6, Data())
        ))
      ))
    ))

  val sixInvitesSeven =
    InvitationTree(1, Data(3.0, hasInvitedBefore = true), Set(
      InvitationTree(2, Data(hasInvitedBefore = true)),
      InvitationTree(3, Data(2.0, hasInvitedBefore = true), Set(
        InvitationTree(4, Data(2.0, hasInvitedBefore = true), Set(
          InvitationTree(5, Data(hasInvitedBefore = true), Set(
            InvitationTree(7, Data())
          )),
          InvitationTree(6, Data(hasInvitedBefore = true))
        ))
      ))
    ))

  val sixInvitesEight =
    InvitationTree(1, Data(3.0, hasInvitedBefore = true), Set(
      InvitationTree(2, Data(hasInvitedBefore = true)),
      InvitationTree(3, Data(2.0, hasInvitedBefore = true), Set(
        InvitationTree(4, Data(2.0, hasInvitedBefore = true), Set(
          InvitationTree(5, Data(hasInvitedBefore = true), Set(
            InvitationTree(7, Data())
          )),
          InvitationTree(6, Data(hasInvitedBefore = true), Set(
            InvitationTree(8, Data())
          ))
        ))
      ))
    ))

  val userByScore: List[(Int, Double)] = List(
    (1,3.0),
    (3,2.0),
    (4,2.0),
    (2,0.0),
    (5,0.0),
    (6,0.0),
    (7,0.0)
  )
}
