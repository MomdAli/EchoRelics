package model

import model.tileImpl.Tile
import model.entity.IEntity

trait ITile {

  val entity: Option[IEntity]

  def isWalkable: Boolean
  def hasPlayer: Boolean
  def hasWall: Boolean
  def hasRelic: Boolean
  def isPlayer(player: IEntity): Boolean
  def isEmpty: Boolean
}

object ITile {
  val emptyTile: Tile = Tile(None)
  def spawnEntity(entity: IEntity): Tile = Tile(Some(entity))

  def apply(entity: Option[IEntity]): ITile = Tile(entity)
}
