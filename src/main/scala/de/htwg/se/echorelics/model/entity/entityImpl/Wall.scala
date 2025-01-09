package model.entity.entityImpl

import model.entity.IEntity

case class Wall() extends IEntity {

  override def id: String = "#"
  override def isWalkable: Boolean = false
  override def isCollectable: Boolean = false
}
