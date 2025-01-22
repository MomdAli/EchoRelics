package utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RandomSpec extends AnyWordSpec with Matchers {

  "A Random" should {

    "generate a random integer within the specified bound" in {
      val rng = Random(42)
      val (value, newRng) = rng.nextInt(100)
      value should be >= 0
      value should be < 100
    }

    "generate a random boolean value" in {
      val rng = Random(42)
      val (value, newRng) = rng.nextBoolean
      value should (be(true) or be(false))
    }

    "generate a random direction" in {
      val rng = Random(42)
      val (direction, newRng) = rng.randomDirection
      Direction.values should contain(direction)
    }

    "generate a random position within the given size" in {
      val rng = Random(42)
      val (position, newRng) = rng.randomPosition(10)
      position.x should be >= 0
      position.x should be < 10
      position.y should be >= 0
      position.y should be < 10
    }

    "shuffle a list of elements" in {
      val rng = Random(42)
      val list = List(1, 2, 3, 4, 5)
      val (shuffledList, newRng) = rng.shuffle(list)
      shuffledList should have length list.length
      shuffledList.toSet should be(list.toSet)
    }
  }
}
