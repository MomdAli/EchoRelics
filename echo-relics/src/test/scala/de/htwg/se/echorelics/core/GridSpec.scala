package de.htwg.se.echorelics.core

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.echorelics.math.{Direction, Position}
import de.htwg.se.echorelics.model.Player

class GridSpec extends AnyWordSpec with Matchers {

  "A Grid" should {

    "return the correct tile for a given position" in {
      val grid = Grid.defaultGrid()
      val position = Position(0, 0)
      val tile = grid.getTile(position)
      tile shouldBe defined
      tile.get.position shouldBe position
    }

    "return None for a position outside the grid" in {
      val grid = Grid.defaultGrid()
      val position = Position(10, 10)
      val tile = grid.getTile(position)
      tile shouldBe None
    }

    "move a player to a new position within bounds" in {
      val grid = Grid.defaultGrid()
      val player = Player.defaultPlayer()
      val newPosition = grid.move(player, Direction.Right)
      newPosition shouldBe defined
      newPosition.get shouldBe Position(1, 0)
    }

    "not move a player to a new position out of bounds" in {
      val grid = Grid.defaultGrid()
      val player = Player.defaultPlayer()
      val newPosition = grid.move(player, Direction.Left)
      newPosition shouldBe None
    }

    "update the tile content when a player moves" in {
      val grid = Grid.defaultGrid()
      val player = Player.defaultPlayer()
      grid.move(player, Direction.Right)
      grid.getTile(Position(0, 0)).get.content shouldBe None
      grid.getTile(Position(1, 0)).get.content shouldBe Some(player)
    }
  }
}