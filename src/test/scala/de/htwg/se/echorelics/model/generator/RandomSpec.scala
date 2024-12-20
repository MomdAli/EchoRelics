package model.generator

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import utils.{Direction, Position}

class RandomSpec extends AnyWordSpec with Matchers {

  "A Random" should {

    "generate a random integer within the specified bound" in {
      val rng = Random(42)
      val (value, newRng) = rng.nextInt(100)
      value should (be >= 0 and be < 100)
      newRng should not be rng
    }

    "generate a random boolean value" in {
      val rng = Random(42)
      val (value, newRng) = rng.nextBoolean
      value should (be(true) or be(false))
      newRng should not be rng
    }

    "generate a random direction" in {
      val rng = Random(42)
      val (direction, newRng) = rng.randomDirection
      Direction.values should contain(direction)
      newRng should not be rng
    }

    "generate a random position within the given size" in {
      val rng = Random(42)
      val (position, newRng) = rng.randomPosition(10)
      position.x should (be >= 0 and be < 10)
      position.y should (be >= 0 and be < 10)
      newRng should not be rng
    }
  }
}
