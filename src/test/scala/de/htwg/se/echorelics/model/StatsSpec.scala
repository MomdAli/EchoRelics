package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class StatsSpec extends AnyWordSpec with Matchers {

  "A Stats" should {

    "have default values" in {
      val stats = Stats()
      stats.score should be(0)
      stats.echoes should be(0)
      stats.health should be(3)
    }

    "update score correctly" in {
      val stats = Stats().updateScore(10)
      stats.score should be(10)
    }

    "update echoes correctly" in {
      val stats = Stats().updateEchoes(5)
      stats.echoes should be(5)
    }

    "update health correctly" in {
      val stats = Stats().updateHealth(-1)
      stats.health should be(2)
    }

    "have a correct string representation" in {
      val stats = Stats(10, 5, 2)
      stats.toString should be("Score: 10\nEchoes: 5\nHealth: 2")
    }
  }
}
