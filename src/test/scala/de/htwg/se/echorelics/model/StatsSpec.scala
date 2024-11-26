package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class StatsSpec extends AnyWordSpec with Matchers {

  "A Stats" should {

    "be created with default values" in {
      val stats = Stats()
      stats.relics should be(0)
      stats.echoes should be(0)
      stats.health should be(3)
    }

    "be created with specified health" in {
      val stats = Stats.withHealth(5)
      stats.relics should be(0)
      stats.echoes should be(0)
      stats.health should be(5)
    }

    "update relics correctly" in {
      val stats = Stats()
      val updatedStats = stats.updateRelics(2)
      updatedStats.relics should be(2)
      updatedStats.echoes should be(0)
      updatedStats.health should be(3)
    }

    "update echoes correctly" in {
      val stats = Stats()
      val updatedStats = stats.updateEchoes(3)
      updatedStats.echoes should be(3)
      updatedStats.relics should be(0)
      updatedStats.health should be(3)
    }

    "update health correctly" in {
      val stats = Stats()
      val updatedStats = stats.updateHealth(-1)
      updatedStats.health should be(2)
      updatedStats.relics should be(0)
      updatedStats.echoes should be(0)
    }
  }
}
