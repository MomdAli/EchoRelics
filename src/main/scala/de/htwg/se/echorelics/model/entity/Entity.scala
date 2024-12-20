package model.entity

trait Entity {
  def id: String
  def isWalkable: Boolean
  def isCollectable: Boolean
}
