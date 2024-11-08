package de.htwg.se.echorelics.core

import de.htwg.se.echorelics.math.Position
import de.htwg.se.echorelics.model.Player
import de.htwg.se.echorelics.math.Direction

case class Grid(tiles: Vector[Vector[Tile]], width: Int, height: Int) {
  def setTileAt(position: Position, tile: Tile): Grid = {
    val newTiles =
      tiles.updated(position.y, tiles(position.y).updated(position.x, tile))
    Grid(newTiles, width, height)
  }

  def getTileAt(position: Position): Tile = {
    tiles(position.y)(position.x)
  }

  def updatePlayerPosition(
      currentPlayer: Player,
      oldPosition: Position
  ): Grid = {
    if (currentPlayer.position.isWithinBounds(width, height)) {
      // Clear old position and set new one
      val oldTile = getTileAt(currentPlayer.position)
      val clearedGrid =
        setTileAt(currentPlayer.position, Tile.PlayerTile(currentPlayer.id))
      clearedGrid.setTileAt(oldPosition, oldTile)
    } else {
      this
    }
  }
}

object Grid {
  def DefaultGrid(players: Player, width: Int, height: Int): Grid = {
    val tiles = Vector.tabulate(width, height) { (x, y) =>
      if (x == players.position.x && y == players.position.y) {
        Tile.PlayerTile(players.id)
      } else {
        Tile.EmptyTile
      }
    }
    Grid(tiles, width, height)
  }
}
