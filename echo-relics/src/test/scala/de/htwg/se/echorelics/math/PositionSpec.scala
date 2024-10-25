package de.htwg.se.echorelics.math

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PositionSpec extends AnyWordSpec with Matchers {
  "A Position" should {
    "add another Position correctly" in {
      val pos1 = Position(1, 2)
      val pos2 = Position(3, 4)
      val result = pos1 + pos2
      result should be(Position(4, 6))
    }

    "check if it is within bounds correctly" in {
      val pos = Position(1, 2)
      pos.isWithinBounds(3, 3) should be(true)
      pos.isWithinBounds(1, 2) should be(false)
    }

    "move correctly in all directions" in {
      val pos = Position(2, 2)
      pos.move(Direction.Up) should be(Position(2, 1))
      pos.move(Direction.Down) should be(Position(2, 3))
      pos.move(Direction.Left) should be(Position(1, 2))
      pos.move(Direction.Right) should be(Position(3, 2))
    }

    "have a proper string representation" in {
      val pos = Position(1, 2)
      pos.toString should be("(1, 2)")
    }
  }
}