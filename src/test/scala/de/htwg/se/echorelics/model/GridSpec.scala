package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import utils.{Direction, Position}
import model.{Grid, Player, Tile, TileContent}

class GridSpec extends AnyWordSpec with Matchers {

  "A Grid" should {

    "be created with a given size" in {
      val grid = new Grid(5)
      grid.size should be(5)
    }

    "set a tile at a specific position" in {
      val grid = new Grid(5)
      val position = Position(2, 2)
      val tile = Tile(TileContent.Wall)
      val newGrid = grid.set(position, tile)
      newGrid.tileAt(position) should be(tile)
    }

    "detect out of bounds positions" in {
      val grid = new Grid(5)
      grid.isOutOfBounds(Position(-1, 0)) should be(true)
      grid.isOutOfBounds(Position(0, -1)) should be(true)
      grid.isOutOfBounds(Position(5, 0)) should be(true)
      grid.isOutOfBounds(Position(0, 5)) should be(true)
      grid.isOutOfBounds(Position(2, 2)) should be(false)
    }

    "move a player to a new position" in {
      val player = Player("1")
      val grid =
        new Grid(5).set(Position(2, 2), Tile(TileContent.Player(player.id)))
      val newGrid = grid.movePlayer(player, Direction.Up, TileContent.Empty)
      newGrid.tileAt(Position(2, 2)).content should be(TileContent.Empty)
      newGrid.tileAt(Position(2, 1)).content should be(
        TileContent.Player(player.id)
      )
    }

    "not move a player out of bounds" in {
      val player = Player("1")
      val grid =
        new Grid(5).set(Position(0, 0), Tile(TileContent.Player(player.id)))
      val newGrid = grid.movePlayer(player, Direction.Left, TileContent.Empty)
      newGrid.tileAt(Position(0, 0)).content should be(
        TileContent.Player(player.id)
      )
    }

    "increase the grid size" in {
      val grid = new Grid(10)
      val newGrid = grid.increaseSize
      newGrid.size should be(11)
    }

    "decrease the grid size" in {
      val grid = new Grid(11)
      val newGrid = grid.decreaseSize
      newGrid.size should be(10)
    }

    "find a player on the grid" in {
      val player = Player("1")
      val grid =
        new Grid(5).set(Position(2, 2), Tile(TileContent.Player(player.id)))
      grid.findPlayer(player) should be(Some(Position(2, 2)))
    }
  }
}
