package utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DirectionSpec extends AnyWordSpec with Matchers {

  "A Direction" should {

    "have an opposite direction" in {
      Direction.Up.opposite should be(Direction.Down)
      Direction.Down.opposite should be(Direction.Up)
      Direction.Left.opposite should be(Direction.Right)
      Direction.Right.opposite should be(Direction.Left)
    }

    "not be its own opposite" in {
      Direction.Up.opposite should not be Direction.Up
      Direction.Down.opposite should not be Direction.Down
      Direction.Left.opposite should not be Direction.Left
      Direction.Right.opposite should not be Direction.Right
    }

    "return to the original direction when opposite is called twice" in {
      Direction.Up.opposite.opposite should be(Direction.Up)
      Direction.Down.opposite.opposite should be(Direction.Down)
      Direction.Left.opposite.opposite should be(Direction.Left)
      Direction.Right.opposite.opposite should be(Direction.Right)
    }
  }
}
