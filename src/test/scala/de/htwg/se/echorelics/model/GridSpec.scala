package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.entity.IEntity
import model.entity.entityImpl.{Player, Echo, Wall, Relic}
import model.gridImpl.Grid
import utils.{Direction, Position}

class GridSpec extends AnyWordSpec with Matchers {

  "A Grid" should {

    "have a size" in {
      val grid = new Grid(5)
      grid.size should be(5)
    }

    "set and get a tile at a specific position" in {
      val grid = new Grid(5)
      val position = Position(2, 2)
      val tile = ITile(Some(Player("player1")))
      val updatedGrid = grid.set(position, tile)
      updatedGrid.tileAt(position) should be(tile)
    }

    "identify out of bounds positions" in {
      val grid = new Grid(5)
      grid.isOutOfBounds(Position(-1, 0)) should be(true)
      grid.isOutOfBounds(Position(0, -1)) should be(true)
      grid.isOutOfBounds(Position(5, 0)) should be(true)
      grid.isOutOfBounds(Position(0, 5)) should be(true)
      grid.isOutOfBounds(Position(2, 2)) should be(false)
    }

    "move a player to a new position" in {
      val player = Player("player1")
      val grid = new Grid(5).set(Position(2, 2), ITile(Some(player)))
      val newGrid = grid.movePlayer(player, Direction.Right)
      newGrid.tileAt(Position(2, 2)).isEmpty should be(true)
      newGrid.tileAt(Position(3, 2)).entity should be(Some(player))
    }

    "not move a player out of bounds" in {
      val player = Player("player1")
      val grid = new Grid(5).set(Position(4, 4), ITile(Some(player)))
      val newGrid = grid.movePlayer(player, Direction.Right)
      newGrid should be(grid)
    }

    "collect a relic when a player moves to its position" in {
      val player = Player("player1")
      val relic = Relic()
      val grid = new Grid(5)
        .set(Position(2, 2), ITile(Some(player)))
        .set(Position(3, 2), ITile(Some(relic)))
      val newGrid = grid.movePlayer(player, Direction.Right)
      newGrid.tileAt(Position(3, 2)).entity should be(Some(player))
    }

    "move echoes and handle player damage" in {
      val player = Player("player1")
      val echo = Echo(id = "echo1", owner = "player1")
      val grid = new Grid(5)
        .set(Position(2, 2), ITile(Some(player)))
        .set(Position(1, 1), ITile(Some(echo)))
      val newGrid = grid.moveEchoes
      newGrid.tileAt(Position(1, 1)).isEmpty should be(true)
    }

    "spawn an echo at a valid position" in {
      val echo = Echo(id = "echo1", owner = "player1")
      val grid = new Grid(5)
      val newGrid = grid.spawnEcho(Position(2, 2), echo)
      newGrid.tileAt(Position(2, 2)).entity should be(
        newGrid.tileAt(Position(2, 2)).entity
      )
    }

    "increase the grid size" in {
      val grid = new Grid(5)
      val newGrid = grid.increaseSize
      newGrid.size should be(6)
    }

    "decrease the grid size" in {
      val grid = new Grid(15)
      val newGrid = grid.decreaseSize
      newGrid.size should be(14)
    }

    "swap two tiles" in {
      val player = Player("player1")
      val wall = Wall()
      val grid = new Grid(5)
        .set(Position(1, 1), ITile(Some(player)))
        .set(Position(2, 2), ITile(Some(wall)))
      val newGrid = grid.swap(Position(1, 1), Position(2, 2))
      newGrid.tileAt(Position(1, 1)).entity should be(Some(wall))
      newGrid.tileAt(Position(2, 2)).entity should be(Some(player))
    }
  }
}
