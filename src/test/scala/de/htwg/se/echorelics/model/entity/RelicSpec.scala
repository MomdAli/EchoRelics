package model.entity

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.item.{HealCard, StrikeCard, SwapPlayerCard, TimeTravelCard}

class RelicSpec extends AnyWordSpec with Matchers {

  "A Relic" should {

    "have id '$'" in {
      val relic = Relic()
      relic.id should be("$")
    }

    "be walkable" in {
      val relic = Relic()
      relic.isWalkable should be(true)
    }

    "be collectable" in {
      val relic = Relic()
      relic.isCollectable should be(true)
    }

    "generate a score between 50 and 100" in {
      val relic = Relic()
      relic.score should (be >= 50 and be <= 100)
    }

    "collect a card based on rarity" in {
      val relic = Relic()
      val collectedCard = relic.collectCard

      collectedCard match {
        case Some(card) =>
          card match {
            case _: HealCard       => succeed
            case _: StrikeCard     => succeed
            case _: SwapPlayerCard => succeed
            case _: TimeTravelCard => succeed
            case _                 => fail("Unexpected card")
          }
        case None =>
          collectedCard should be(None)
      }
    }
  }
}
