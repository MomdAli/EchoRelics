package com.echorelics.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import com.echorelics.core.Grid
import com.echorelics.math.Position

class PlayerSpec extends AnyWordSpec {
  "A Player" when {
    "default" should {
      "be created with a default position" in {
        val grid = Grid.defaultGrid()
        val player = Player.defaultPlayer(grid)
        player.position shouldBe Position(0, 0)
      }
    }
  }
}
