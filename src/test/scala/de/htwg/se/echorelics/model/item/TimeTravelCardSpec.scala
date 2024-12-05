package model.item

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import service.GameManager
import model.events.{EventManager, GameEvent}
import model.events.EventListener

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

    "notify the EventManager when played" in {
      val card = TimeTravelCard()
      val gameManager = mock[GameManager]
      EventManager.subscribe(new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          event match {
            case GameEvent.OnTimeTravelEvent(turns) => turns should be(6)
            case _                                  => fail("Unexpected event")
          }
        }
      })
      card.play(gameManager)
    }

    "return the same GameManager instance when played" in {
      val card = TimeTravelCard()
      val gameManager = mock[GameManager]
      card.play(gameManager) should be(gameManager)
    }
  }
}
