package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.entity.{Player, Relic, Echo}
import utils.{Direction, Position}

class GridSpec extends AnyWordSpec with Matchers {

  "A Grid" should {

    "have the correct size" in {
      val grid = new Grid(10)
      grid.size should be(10)
    }

    "initialize with empty tiles" in {
      val grid = new Grid(10)
      for {
        y <- 0 until 10
        x <- 0 until 10
      } yield grid.tileAt(Position(x, y)).isEmpty should be(true)
    }

    "set a tile correctly" in {
      val grid = new Grid(10)
      val position = Position(2, 3)
      val tile = Tile(Some(Player("player1")))
      val newGrid = grid.set(position, tile)
      newGrid.tileAt(position) should be(tile)
    }

    "detect out of bounds positions" in {
      val grid = new Grid(10)
      grid.isOutOfBounds(Position(-1, 0)) should be(true)
      grid.isOutOfBounds(Position(0, -1)) should be(true)
      grid.isOutOfBounds(Position(10, 0)) should be(true)
      grid.isOutOfBounds(Position(0, 10)) should be(true)
      grid.isOutOfBounds(Position(5, 5)) should be(false)
    }

    "move a player correctly" in {
      val grid = new Grid(10)
      val player = Player("player1")
      val startPosition = Position(2, 2)
      val endPosition = Position(2, 3)
      val gridWithPlayer = grid.set(startPosition, Tile(Some(player)))
      val newGrid = gridWithPlayer.movePlayer(player, Direction.Down)
      newGrid.tileAt(startPosition).isEmpty should be(true)
      newGrid.tileAt(endPosition).entity.contains(player) should be(true)
    }

    "not move a player out of bounds" in {
      val grid = new Grid(10)
      val player = Player("player1")
      val startPosition = Position(0, 0)
      val gridWithPlayer = grid.set(startPosition, Tile(Some(player)))
      val newGrid = gridWithPlayer.movePlayer(player, Direction.Up)
      newGrid.tileAt(startPosition).entity.contains(player) should be(true)
    }

    "increase size correctly" in {
      val grid = new Grid(10)
      val newGrid = grid.increaseSize
      newGrid.size should be(11)
    }

    "not increase size beyond 20" in {
      val grid = new Grid(20)
      val newGrid = grid.increaseSize
      newGrid.size should be(20)
    }

    "decrease size correctly" in {
      val grid = new Grid(15)
      val newGrid = grid.decreaseSize
      newGrid.size should be(14)
    }

    "not decrease size below 10" in {
      val grid = new Grid(10)
      val newGrid = grid.decreaseSize
      newGrid.size should be(10)
    }

    "swap tiles correctly" in {
      val grid = new Grid(10)
      val pos1 = Position(1, 1)
      val pos2 = Position(2, 2)
      val tile1 = Tile(Some(Player("player1")))
      val tile2 = Tile(Some(Relic()))
      val gridWithTiles = grid.set(pos1, tile1).set(pos2, tile2)
      val newGrid = gridWithTiles.swap(pos1, pos2)
      newGrid.tileAt(pos1) should be(tile2)
      newGrid.tileAt(pos2) should be(tile1)
    }

    "find a player correctly" in {
      val grid = new Grid(10)
      val player = Player("player1")
      val position = Position(2, 3)
      val gridWithPlayer = grid.set(position, Tile(Some(player)))
      gridWithPlayer.findPlayer(player) should be(Some(position))
    }
  }
}
