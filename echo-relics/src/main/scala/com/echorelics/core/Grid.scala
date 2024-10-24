package com.echorelics.core

import com.echorelics.math.{Direction, Position}
import com.echorelics.model.Player

final case class Grid(tiles: List[Tile], width: Int = 5, height: Int = 5) {

  def getTile(position: Position): Option[Tile] = {
    tiles.find(tile => tile.position == position)
  }

  def move(player: Player, direction: Direction): Option[Position] = {
    val newPosition = player.position.move(direction)
    if (newPosition.isWithinBounds(width, height))
      getTile(player.position).foreach(tile => tile.content = None)
      getTile(newPosition).foreach(tile => tile.content = Some(player))
      Some(newPosition)
    else None
  }
}

object Grid {
  def defaultGrid(): Grid = {
    // Creating an empty 10x10 grid of tiles

    val width = 10
    val height = 10

    val tiles = for {
      y <- 0 until height
      x <- 0 until width
    } yield Tile(Position(x, y))

    val grid = new Grid(tiles.toList, width, height)

    // TODO: Place relics on specific tiles

    grid
  }
}
