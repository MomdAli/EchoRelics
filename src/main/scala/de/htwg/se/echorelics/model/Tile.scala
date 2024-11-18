package model

enum TileContent(val symbol: String) {
  case Empty extends TileContent("   ")
  case Wall extends TileContent(" # ")
  case Player(val id: String) extends TileContent(s" $id ")
  case Out extends TileContent(" X ")
}

case class Tile(content: TileContent) {

  // Determines if the tile is walkable
  def isWalkable: Boolean = content match {
    case TileContent.Empty => true
    case _                 => false
  }

  // Checks if the tile contains a player
  def hasPlayer: Boolean = content == TileContent.Player

  def isPlayer(player: Player): Boolean = content match {
    case TileContent.Player(id) => id == player.id
    case _                      => false
  }
}

object Tile {
  def EmptyTile: Tile = Tile(TileContent.Empty)
  def WallTile: Tile = Tile(TileContent.Wall)
  def PlayerTile(player: Player): Tile = Tile(TileContent.Player(player.id))
}
