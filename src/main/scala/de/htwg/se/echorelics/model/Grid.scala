package model

import model.Tile
import model.entity.{Echo, Player, Relic}
import utils.{Direction, Position}
import model.events.{EventManager, GameEvent}

case class Grid(tiles: Vector[Vector[Tile]]) {

  def this(size: Int) = this(Vector.fill(size, size)(Tile.EmptyTile))

  def set(position: Position, tile: Tile): Grid = {
    copy(tiles.updated(position.y, tiles(position.y).updated(position.x, tile)))
  }

  def size: Int = tiles.size

  def tileAt(position: Position): Tile = {
    if (isOutOfBounds(position)) {
      Tile.EmptyTile
    } else {
      tiles(position.y)(position.x)
    }
  }

  def isOutOfBounds(position: Position): Boolean = {
    position.x < 0 || position.y < 0 || position.x >= size || position.y >= size
  }

  def movePlayer(
      player: Player,
      direction: Direction
  ): Grid = {
    findPlayer(player) match {
      case Some(playerPosition) =>
        val newPosition = playerPosition.move(direction)
        if (isOutOfBounds(newPosition) || !tileAt(newPosition).isWalkable) {
          this
        } else {
          val newGrid = set(playerPosition, Tile.EmptyTile)
          checkCollect(player, newPosition)
          newGrid.set(newPosition, Tile(Some(player)))
        }
      case None => this
    }
  }

  private def checkCollect(player: Player, position: Position) = {
    tileAt(position) match {
      case Tile(Some(Relic())) =>
        EventManager.notify(GameEvent.OnRelicCollectEvent(player, Relic()))
      case _ =>
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

  def swap(pos1: Position, pos2: Position): Grid = {
    val tile1 = tileAt(pos1)
    val tile2 = tileAt(pos2)
    set(pos1, tile2).set(pos2, tile1)
  }
}
