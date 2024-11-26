package model

import utils.{Direction, Position}
import view.DisplayRenderer
import model.Tile
import model.generator.{Random, Spawner}

case class Grid(tiles: Vector[Vector[Tile]]) {

  def this(size: Int) = this(Vector.fill(size, size)(Tile(TileContent.Empty)))

  def setupGrid(players: List[Player], wallRatio: Int): Grid = {
    val newGrid =
      Spawner.generateWalls(this, System.currentTimeMillis(), wallRatio)

    players.zipWithIndex.foldLeft(newGrid) { case (grid, (player, _)) =>
      val position = Iterator
        .iterate(
          Random(System.currentTimeMillis().toInt).randomPosition(size)._1
        ) { pos =>
          Random(System.currentTimeMillis().toInt).randomPosition(size)._1
        }
        .dropWhile { pos =>
          isOutOfBounds(pos) || !grid.tileAt(pos).isWalkable
        }
        .next()
      grid.set(position, Tile(TileContent.Player(player.id)))
    }
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

  def isOutOfBounds(position: Position): Boolean = {
    position.x < 0 || position.y < 0 || position.x >= size || position.y >= size
  }

  def movePlayer(
      player: Player,
      direction: Direction,
      drop: TileContent
  ): Grid = {
    findPlayer(player) match {
      case Some(playerPosition) =>
        val newPosition = playerPosition.move(direction)
        if (isOutOfBounds(newPosition) || !tileAt(newPosition).isWalkable) {
          this
        } else {
          val newGrid = set(playerPosition, Tile(drop))
          newGrid.set(newPosition, Tile(TileContent.Player(player.id)))
        }
      case None => this
    }
  }

  // TODO: moveEcho should move an echo to a new position
  def moveEcho(echo: Echo): Grid = { this }

  def findPlayer(player: Player): Option[Position] = {
    val position = for {
      y <- 0 until size
      x <- 0 until size
      if tileAt(Position(x, y)).isPlayer(player)
    } yield Position(x, y)
    position.headOption
  }

  def increaseSize: Grid = {
    if (size >= 20) {
      this
    } else
      new Grid(size + 1)
  }

  def decreaseSize: Grid = {
    if (size <= 10) {
      this
    } else
      new Grid(size - 1)
  }

  override def toString: String = {
    DisplayRenderer.render(this)
  }
}
