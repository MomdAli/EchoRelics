package de.htwg.se.echorelics.core

sealed trait TileContent
object TileContent {
  case object Empty extends TileContent
  case object Wall extends TileContent
  case object Player extends TileContent
}

case class Tile(content: TileContent) {

  // Determines if the tile is walkable
  def isWalkable: Boolean = content match {
    case TileContent.Empty => true
    case _                 => false
  }

  // Checks if the tile contains a player
  def hasPlayer: Boolean = content match {
    case TileContent.Player => true
    case _                  => false
  }
}

object Tile {
  def EmptyTile: Tile = Tile(TileContent.Empty)
  def WallTile: Tile = Tile(TileContent.Wall)
  def PlayerTile(id: String): Tile = Tile(TileContent.Player)
}
