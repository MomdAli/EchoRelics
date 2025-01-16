package model.gridImpl

import com.google.inject.Inject

import model.{IGrid, ITile}
import model.entity.IEntity
import model.events.{EventManager, GameEvent}
import utils.{Direction, Position}

case class Grid @Inject() (tiles: Vector[Vector[ITile]] = Vector.empty)
    extends IGrid {

  def this(size: Int) = this(Vector.fill(size, size)(ITile.emptyTile))

  override def set(position: Position, tile: ITile): Grid = {
    copy(tiles.updated(position.y, tiles(position.y).updated(position.x, tile)))
  }

  override def size: Int = tiles.size

  override def tileAt(position: Position): ITile = {
    if (isOutOfBounds(position)) {
      ITile.emptyTile
    } else {
      tiles(position.y)(position.x)
    }
  }

  override def isOutOfBounds(position: Position): Boolean = {
    position.x < 0 || position.y < 0 || position.x >= size || position.y >= size
  }

  override def movePlayer(
      player: IEntity,
      direction: Direction
  ): Grid = {
    findPlayer(player) match {
      case Some(playerPosition) =>
        val newPosition = playerPosition.move(direction)
        if (isOutOfBounds(newPosition) || !tileAt(newPosition).isWalkable) {
          this
        } else {
          val newGrid = set(playerPosition, ITile.emptyTile)
          checkCollect(player, newPosition)
          newGrid.set(newPosition, ITile(Some(player)))
        }
      case None => this
    }
  }

  private def checkCollect(player: IEntity, position: Position) = {
    if (tileAt(position).entity.exists(_.isCollectable)) {
      val relic = tileAt(position).entity.get
      EventManager.notify(
        GameEvent.OnRelicCollectEvent(player, relic)
      )
    }
  }

  // TODO: moveEcho should move an echo to a new position
  override def moveEcho(echo: IEntity): Grid = { this }

  override def findPlayer(player: IEntity): Option[Position] = {
    val position = for {
      y <- 0 until size
      x <- 0 until size
      if tileAt(Position(x, y)).isPlayer(player)
    } yield Position(x, y)
    position.headOption
  }

  override def increaseSize: Grid = {
    if (size >= 20) {
      this
    } else
      new Grid(size + 1)
  }

  override def decreaseSize: Grid = {
    if (size <= 10) {
      this
    } else
      new Grid(size - 1)
  }

  override def swap(pos1: Position, pos2: Position): Grid = {
    val tile1 = tileAt(pos1)
    val tile2 = tileAt(pos2)
    set(pos1, tile2).set(pos2, tile1)
  }
}
