package model.tileImpl

import model.entity.IEntity
import model.ITile

case class Tile(entity: Option[IEntity]) extends ITile {

  // Determines if the tile is walkable
  override def isWalkable: Boolean = {
    entity.forall(_.isWalkable)
  }

  // Checks if the tile contains a player
  override def hasPlayer: Boolean = entity.exists(IEntity.isPlayer)
  override def hasWall: Boolean = entity.exists(IEntity.isWall)
  override def hasRelic: Boolean = entity.exists(IEntity.isRelic)

  override def isPlayer(player: IEntity): Boolean = {
    entity.exists(e => IEntity.isPlayer(e) && e.id == player.id)
  }

  override def isEmpty: Boolean = entity.isEmpty || entity.contains(IEntity.empty)
}
