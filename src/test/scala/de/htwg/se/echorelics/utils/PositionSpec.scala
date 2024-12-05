package utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PositionSpec extends AnyWordSpec with Matchers {

  "A Position" should {

    "add another position correctly" in {
      val pos1 = Position(1, 2)
      val pos2 = Position(3, 4)
      (pos1 + pos2) should be(Position(4, 6))
    }

    "check if within bounds correctly" in {
      val pos = Position(5, 5)
      pos.isWithinBounds(10, 10) should be(true)
      pos.isWithinBounds(5, 5) should be(false)
    }

    "move in the correct direction" in {
      val pos = Position(2, 2)
      pos.move(Direction.Up) should be(Position(2, 1))
      pos.move(Direction.Down) should be(Position(2, 3))
      pos.move(Direction.Left) should be(Position(1, 2))
      pos.move(Direction.Right) should be(Position(3, 2))
    }

    "have a correct string representation" in {
      val pos = Position(2, 3)
      pos.toString should be("(2, 3)")
    }

    "have predefined positions" in {
      Position.Zero should be(Position(0, 0))
      Position.Up should be(Position(0, -1))
      Position.Down should be(Position(0, 1))
      Position.Left should be(Position(-1, 0))
      Position.Right should be(Position(1, 0))
    }
  }
}
