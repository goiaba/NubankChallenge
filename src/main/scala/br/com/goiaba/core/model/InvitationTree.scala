package br.com.goiaba.core.model

import scala.annotation.tailrec

/**
  * Created by bruno on 3/13/16.
  */
case class Data(score: Double = 0.0, hasInvitedBefore: Boolean = false)

case class InvitationTree(key: Int, data: Data = Data(), children: Set[InvitationTree] = Set())
  extends MWTree[Int, Data, InvitationTree] {

  val scoreMultiplier = 0.5

  override def find(key: Int): Option[InvitationTree] = {
    @tailrec def findRec(trees: List[InvitationTree]): Option[InvitationTree] = trees match {
      case Nil => None
      case (h :: t) =>
        if (key == h.key) Option(h)
        else findRec(t ::: h.children.toList)
    }
    findRec(List(this))
  }

  override def pathTo(key: Int): List[InvitationTree] = {
    def pathToRec(currTree: InvitationTree, currPath: List[InvitationTree]): List[InvitationTree] = {
      if (key == currTree.key) currTree :: currPath
      else currTree.children.map(pathToRec(_, currTree :: currPath)).toList.flatten
    }
    pathToRec(this, List())
  }

  /*
   * When inserting, the following rules will be applied:
   *
   * p: the key to be inserted already exists in the tree
   * q: the tree represented by parentKey has invited before
   *
   *  p &&  q => Do nothing
   *  p && !q => Update the trees from parent up to the root
   * !p &&  q => Insert the new tree in the parent's children list.
   * !p && !q => Insert and Update
   *
   * The 'data' argument is ignored, since all the information it
   * could carry are calculated here.
   */
  override def insert(key: Int, data: Data, parentKey: Int): InvitationTree = find(parentKey) match {
    case None => throw new RuntimeException("parentKey [" + parentKey + "] does not exist.")
    case Some(parentTree) => find(key) match {
      case Some(tree) => if (parentTree.data.hasInvitedBefore) this else updateTree(parentTree)
      case None => updateTree(parentTree, Option(key))
    }
  }

  def insert(key: Int, parentKey: Int): InvitationTree = insert(key, Data(), parentKey)

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

  protected def updateData(data: Data, hasInvitedBefore: Boolean, level: Int): Data = hasInvitedBefore match {
    case true => data
    case false => Data(data.score + math.pow(scoreMultiplier, level), true)
  }

  protected def updateTree(parentTree: InvitationTree, key: Option[Int] = None): InvitationTree = {
    val newParentTree = key match {
      case None => parentTree.copy(data = parentTree.data.copy(hasInvitedBefore = true))
      case Some(k) => parentTree.copy(data = parentTree.data.copy(hasInvitedBefore = true),
    children = parentTree.children + InvitationTree(k))
    }
    val pathToParent = pathTo(parentTree.key)
    //pathToParent is guaranteed to be a List so we can call tail
    pathToParent.tail.foldLeft(newParentTree)((acc, item) => {
      //since we are here, inside the foldLeft, and considering it was applied over
      // the tail of pathToParent, we are guaranteed that there is a prior item (head).
      //
      // indexOfPriorItem will be used to retrieve the outdated InvitationTree,
      // priorOutdatedItem, that must be removed from the children's list of the
      // current item. After this removal, a new InvitationTree will be generated
      // with its children's list consisting of:
      //   item.children diff Set(priorOutdatedItem) + acc
      // where acc represents the InvitationTree created in the previous step of foldLeft.
      //
      // indexOfPriorItem will also be used as the level to update the score of the item
      val indexOfPriorItem = pathToParent.indexOf(item)-1
      val priorOutdatedItem = pathToParent.lift(indexOfPriorItem).get
      InvitationTree(item.key,
        updateData(item.data, parentTree.data.hasInvitedBefore, indexOfPriorItem),
        (item.children diff Set(priorOutdatedItem)) + acc)
    })
  }

}
