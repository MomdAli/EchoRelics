package com.echorelics.math

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class PositionSpec extends AnyWordSpec {
  "A Position" when {
    "is created" should {
      "have correct coordinates" in {
        val position = Position(1, 1)
        position.x shouldBe 1
        position.y shouldBe 1
      }
    }

    "is moved" should {
      "have correct coordinates" in {
        val position = Position(1, 1)
        val newPosition = position.move(Direction.Up)
        newPosition.x shouldBe 1
        newPosition.y shouldBe 0
      }
    }
  }
}
