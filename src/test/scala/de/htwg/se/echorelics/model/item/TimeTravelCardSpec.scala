package model.item

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import service.GameManager
import model.events.{EventManager, EventListener, GameEvent}

class TimeTravelCardSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A TimeTravelCard" should {

    "have the correct rarity" in {
      val card = TimeTravelCard()
      card.rarity should be(Rarity.Uncommon)
    }

    "have the correct name" in {
      val card = TimeTravelCard()
      card.name should be("Time Travel")
    }

    "have the correct description" in {
      val card = TimeTravelCard()
      card.description should be("Let's you go back in time.")
    }

    "return the same game manager instance when played" in {
      val card = TimeTravelCard()
      val gameManager = mock[GameManager]

      val result = card.play(gameManager)

      result should be(gameManager)
    }
  }
}
