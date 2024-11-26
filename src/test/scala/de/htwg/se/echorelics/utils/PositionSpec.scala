package utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PositionSpec extends AnyWordSpec with Matchers {

  "A Position" should {

    "add another position correctly" in {
      val pos1 = Position(1, 2)
      val pos2 = Position(3, 4)
      val result = pos1 + pos2
      result should be(Position(4, 6))
    }

    "check if within bounds correctly" in {
      val pos = Position(2, 3)
      pos.isWithinBounds(5, 5) should be(true)
      pos.isWithinBounds(2, 3) should be(false)
    }

    "move up correctly" in {
      val pos = Position(2, 2)
      val result = pos.move(Direction.Up)
      result should be(Position(2, 1))
    }

    "move down correctly" in {
      val pos = Position(2, 2)
      val result = pos.move(Direction.Down)
      result should be(Position(2, 3))
    }

    "move left correctly" in {
      val pos = Position(2, 2)
      val result = pos.move(Direction.Left)
      result should be(Position(1, 2))
    }

    "move right correctly" in {
      val pos = Position(2, 2)
      val result = pos.move(Direction.Right)
      result should be(Position(3, 2))
    }

    "return correct string representation" in {
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
