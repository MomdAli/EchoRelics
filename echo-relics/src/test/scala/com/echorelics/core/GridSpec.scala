package com.echorelics.core

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import com.echorelics.math.Position

class GridSpec extends AnyWordSpec {
  "A Grid" when {
    "default" should {
      "have a width and height of 10" in {
        val grid = Grid.defaultGrid()
        grid.width shouldBe 10
        grid.height shouldBe 10
      }

      "contain 100 tiles" in {
        val grid = Grid.defaultGrid()
        grid.tiles.size shouldBe 100
      }
    }

    "getting a tile" should {
      "return the correct tile for a given position" in {
        val grid = Grid.defaultGrid()
        val position = Position(1, 1)
        val tile = grid.getTile(position)
        tile.get.position shouldBe position
      }
    }
  }
}
