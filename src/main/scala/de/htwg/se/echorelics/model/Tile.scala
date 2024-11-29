package model

import model.entity.{Entity, Player, Wall, Relic}
case class Tile(entity: Option[Entity]) {

  // Determines if the tile is walkable
  def isWalkable: Boolean = {
    entity.forall(_.isWalkable)
  }

  // Checks if the tile contains a player
  def hasPlayer: Boolean = entity.exists(_.isInstanceOf[Player])
  def hasWall: Boolean = entity.exists(_.isInstanceOf[Wall])
  def hasRelic: Boolean = entity.exists(_.isInstanceOf[Relic])

  def isPlayer(player: Player): Boolean = entity match {
    case Some(Player(id, _, _)) => id == player.id
    case _                      => false
  }

  def isEmpty: Boolean = entity.isEmpty
}

object Tile {
  def EmptyTile: Tile = Tile(None)
  def WallTile: Tile = Tile(Some(Wall()))
  def PlayerTile(player: Player): Tile = Tile(Some(player))
}
