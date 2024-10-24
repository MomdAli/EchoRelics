package com.echorelics.core

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class TileSpec extends AnyWordSpec {
  "A Tile" when {
    "default" should {
      "not be occupied" in {
        val tile = Tile()
        tile.isOccupied() shouldBe false
      }
    }

    "setting content" should {
      "occupy the tile" in {
        val tile = Tile()
        tile.content = Some("test")
        tile.isOccupied() shouldBe true
      }
    }
  }
}
