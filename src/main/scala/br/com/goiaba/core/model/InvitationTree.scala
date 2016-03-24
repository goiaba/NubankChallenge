package br.com.goiaba.core.model

import scala.annotation.tailrec

/**
  * Created by bruno on 3/13/16.
  */
case class Data(score: Double = 0.0, hasInvitedBefore: Boolean = false)

case class InvitationTree(key: Int, data: Data = Data(), children: Set[InvitationTree] = Set())
  extends MWTree[Int, Data, InvitationTree] {

  val scoreMultiplier = 0.5

  def find(key: Int): Option[InvitationTree] = {
    @tailrec def findRec(trees: List[InvitationTree]): Option[InvitationTree] = trees match {
      case Nil => None
      case (h :: t) =>
        if (key == h.key) Option(h)
        else findRec(t ::: h.children.toList)
    }
    findRec(List(this))
  }

  def pathTo(key: Int): List[InvitationTree] = {
    def findPathRec(currTree: InvitationTree, currPath: List[InvitationTree]): List[InvitationTree] = {
      if (key == currTree.key) currTree :: currPath
      else currTree.children.toList.flatMap(findPathRec(_, currTree :: currPath))
    }
    findPathRec(this, List())
  }

  override def insert(key: Int, parentKey: Int): InvitationTree = find(parentKey) match {
    case None => throw new RuntimeException("parentKey [" + parentKey + "] does not exist.")
    case Some(parentTree) => {
      val pathToParent = pathTo(parentKey)
      pathToParent match {
        case Nil => throw new RuntimeException("Unexpected behavior on findPath method. Path to parent not found.")
        case (h :: t) => {
          val z = find(key) match {
            case None => parentTree.copy(data = parentTree.data.copy(hasInvitedBefore = true),
              children = parentTree.children + InvitationTree(key))
            case Some(existingTree) => parentTree.copy(data = parentTree.data.copy(hasInvitedBefore = true))
          }
          t.foldLeft(z)((acc, item) => {
            val indexOfCurrentItem = pathToParent.indexOf(item)
            val priorOutdatedItem = pathToParent.lift(indexOfCurrentItem-1).get
            InvitationTree(item.key,
              scoreFunction(item.data, parentTree.data.hasInvitedBefore, indexOfCurrentItem-1),
              item.children.&~(Set(priorOutdatedItem)) + acc)
          })
        }
      }
    }
  }

  protected def scoreFunction(data: Data, hasInvitedBefore: Boolean, level: Int): Data = hasInvitedBefore match {
    case true => data
    case false => Data(data.score + math.pow(scoreMultiplier, level), true)
  }

  def ranking: List[(Int, Double)] = {
    @tailrec def rankingRec(trees: List[InvitationTree], l: List[(Int, Double)]): List[(Int, Double)] = trees match {
      case Nil => l
      case (h :: t) => {
        if (h.data.hasInvitedBefore) rankingRec(t ::: h.children.toList, (h.key, h.data.score) :: l)
        else rankingRec(t ::: h.children.toList, l)
      }
    }
    rankingRec(List(this), List()).sortWith(_._2 > _._2)
  }

}
