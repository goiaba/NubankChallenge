package br.com.goiaba.core.model

/**
  * Created by bruno on 3/13/16.
  */
case class Data(score: Double = 0.0, hasInvitedBefore: Boolean = false)

case class InvitationTree(key: Int, data: Data = Data(), children: Set[InvitationTree] = Set())
  extends MWTree[Int, Data, InvitationTree] {

  val scoreMultiplier = 0.5

  def find(key: Int): Option[InvitationTree] = key match {
    case this.key => Some(this)
    case _ => this.children.toList.flatMap(_.find(key)) match {
      case Nil => None
      case (h::_) => Some(h)
    }
  }

  def findPath(key: Int): List[InvitationTree] = {
    def findPathRec(currTree: InvitationTree, currPath: List[InvitationTree]): List[InvitationTree] = key match {
      case currTree.key => currTree :: currPath
      case _ => currTree.children.toList.flatMap(findPathRec(_, currTree :: currPath))
    }
    findPathRec(this, List())
  }

  override def insert(key: Int, parentKey: Int): InvitationTree = {
    val p = find(parentKey).getOrElse(throw new RuntimeException("parentKey [" + parentKey + "] does not exist."))
    val pathToParent = findPath(parentKey)
    pathToParent match {
      case Nil => throw new RuntimeException("Unexpected behavior on findPath method. Path to parent not found.")
      case (h::t) => {
        val z = find(key).isDefined match {
          case true => p.copy(data = p.data.copy(hasInvitedBefore = true))
          case _ => p.copy(data = p.data.copy(hasInvitedBefore = true), children = p.children + InvitationTree(key, Data()))
        }
        t.foldLeft(z)((acc, item) => {
          val indexOfCurrentItem = pathToParent.indexOf(item)
          val priorOutdatedItem = pathToParent.lift(indexOfCurrentItem-1).get
          InvitationTree(item.key,
            scoreFunction(item.data, p.data.hasInvitedBefore, indexOfCurrentItem-1),
            item.children.&~(Set(priorOutdatedItem)) + acc)
        })
      }
    }
  }

  def scoreFunction(data: Data, hasInvitedBefore: Boolean, level: Int): Data = hasInvitedBefore match {
    case true => data
    case false => Data(data.score + math.pow(scoreMultiplier, level), true)
  }

  def ranking: List[(Int, Double)] = {
    def rankingRec(t: InvitationTree, l: List[(Int, Double)]): List[(Int, Double)] = {
      (t.key, t.data.score) :: t.children.filter(_.data.hasInvitedBefore).flatMap(
        child => rankingRec(child, (child.key, child.data.score) :: l)
      ).toList
    }
    this.data.hasInvitedBefore match {
      case true => rankingRec(this, Nil).sortWith(_._2 > _._2)
      case _ => Nil
    }
  }
}
