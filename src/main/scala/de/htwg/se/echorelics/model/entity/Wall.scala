package model.entity

case class Wall() extends Entity {

  override def id: String = "#"
  override def isWalkable: Boolean = false
  override def isCollectable: Boolean = false
}
