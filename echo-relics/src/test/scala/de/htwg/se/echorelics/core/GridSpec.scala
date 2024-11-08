package de.htwg.se.echorelics.core

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.echorelics.math.Position
import de.htwg.se.echorelics.model.Player
import de.htwg.se.echorelics.math.Direction

class GridSpec extends AnyWordSpec with Matchers {
  "A Grid" should {
    "set a tile at a specific position" in {
      val grid = Grid(Vector.fill(3, 3)(Tile.EmptyTile), 3, 3)
      val newTile = Tile.PlayerTile("P")
      val newPosition = Position(1, 1)
      val newGrid = grid.setTileAt(newPosition, newTile)
      newGrid.getTileAt(newPosition) should be(newTile)
    }

    "get a tile at a specific position" in {
      val tile = Tile.PlayerTile("P")
      val grid = Grid(
        Vector
          .fill(3, 3)(Tile.EmptyTile)
          .updated(1, Vector(Tile.EmptyTile, tile, Tile.EmptyTile)),
        3,
        3
      )
      val position = Position(1, 1)
      grid.getTileAt(position) should be(tile)
    }

    "update player position within bounds" in {
      val player = Player("P", Position(0, 0))
      val grid = Grid.DefaultGrid(player, 3, 3)
      val newPosition = Position(1, 1)
      val updatedPlayer = player.copy(position = newPosition)
      val newGrid = grid.updatePlayerPosition(updatedPlayer, player.position)
      newGrid.getTileAt(newPosition) should be(Tile.PlayerTile(player.id))
      newGrid.getTileAt(player.position) should be(Tile.EmptyTile)
    }

    "not update player position out of bounds" in {
      val player = Player("P", Position(0, 0))
      val grid = Grid.DefaultGrid(player, 3, 3)
      val newPosition = Position(3, 3)
      val updatedPlayer = player.copy(position = newPosition)
      val newGrid = grid.updatePlayerPosition(updatedPlayer, player.position)
      newGrid should be(grid)
    }

    "create a default grid with a player at the correct position" in {
      val player = Player("P", Position(1, 1))
      val grid = Grid.DefaultGrid(player, 3, 3)
      grid.getTileAt(player.position) should be(Tile.PlayerTile(player.id))
    }
  }
}
