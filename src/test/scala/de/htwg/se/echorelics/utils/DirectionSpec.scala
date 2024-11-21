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
    }
}