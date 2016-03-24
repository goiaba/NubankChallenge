package br.com.goiaba.core.model

/**
  * Created by bruno on 3/12/16.
  */
trait MWTree[K, A, T <: MWTree[K,A,T]] {
  def find(key: K): Option[T]
  def pathTo(key: K): List[T]
  def insert(key: K, parentKey: K): T
}