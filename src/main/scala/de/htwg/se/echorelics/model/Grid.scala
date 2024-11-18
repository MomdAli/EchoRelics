package model

import utils.{Position, Direction, DisplayRenderer}
import model.{Tile, TileContent, Player}

case class Grid(tiles: Vector[Vector[Tile]]) {

  def this(size: Int) = this(Vector.fill(size, size)(Tile(TileContent.Empty)))

  def setupGrid(players: List[Player]): Grid = {
    val random = new scala.util.Random
    val newGrid = players.zipWithIndex.foldLeft(this) {
      case (grid, (player, index)) =>
        var position = Position(random.nextInt(size), random.nextInt(size))
        while (isOutOfBounds(position)) {
          position = Position(random.nextInt(size), random.nextInt(size))
        }
        grid.set(position, Tile(TileContent.Player(player.id)))
    }
    newGrid
  }

  def set(position: Position, tile: Tile): Grid = {
    copy(tiles.updated(position.y, tiles(position.y).updated(position.x, tile)))
  }

  def size: Int = tiles.size

  def tileAt(position: Position): Tile = {
    if (isOutOfBounds(position)) {
      Tile(TileContent.Out)
    } else {
      tiles(position.y)(position.x)
    }
  }

  private def isOutOfBounds(position: Position): Boolean = {
    position.x < 0 || position.y < 0 || position.x >= size || position.y >= size
  }

  def movePlayer(player: Player, direction: Direction): Grid = {
    val playerPosition = findPlayer(player)
    val newPosition = playerPosition.move(direction)
    if (isOutOfBounds(newPosition) || !tileAt(newPosition).isWalkable) {
      this
    } else {
      val newGrid = set(playerPosition, Tile(TileContent.Empty))
      newGrid.set(newPosition, Tile(TileContent.Player(player.id)))
    }
  }

  private def findPlayer(player: Player): Position = {
    val position = for {
      y <- 0 until size
      x <- 0 until size
      if tileAt(Position(x, y)).isPlayer(player)
    } yield Position(x, y)
    position.head
  }

  override def toString: String = {
    DisplayRenderer.render(this)
  }
}
