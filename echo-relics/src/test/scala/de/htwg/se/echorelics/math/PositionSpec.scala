package de.htwg.se.echorelics.math

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PositionSpec extends AnyWordSpec with Matchers {
  "A Position" should {
    "add another position correctly" in {
      val pos1 = Position(1, 2)
      val pos2 = Position(3, 4)
      (pos1 + pos2) should be(Position(4, 6))
    }

    "check if it is within bounds correctly" in {
      val pos = Position(1, 2)
      pos.isWithinBounds(3, 3) should be(true)
      pos.isWithinBounds(1, 2) should be(false)
    }

    "move correctly in all directions" in {
      val pos = Position(1, 1)
      pos.move(Direction.Up) should be(Position(1, 0))
      pos.move(Direction.Down) should be(Position(1, 2))
      pos.move(Direction.Left) should be(Position(0, 1))
      pos.move(Direction.Right) should be(Position(2, 1))
    }

    "have a correct string representation" in {
      val pos = Position(1, 2)
      pos.toString should be("(1, 2)")
    }
  }

  "The Position object" should {
    "have predefined positions" in {
      Position.Zero should be(Position(0, 0))
      Position.Up should be(Position(0, -1))
      Position.Down should be(Position(0, 1))
      Position.Left should be(Position(-1, 0))
      Position.Right should be(Position(1, 0))
    }
  }
}
