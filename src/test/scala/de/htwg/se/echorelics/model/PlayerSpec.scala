package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.events.{EventManager, GameEvent}
import model.events.EventListener

class PlayerSpec extends AnyWordSpec with Matchers {

  "A Player" should {

    "be created with a given id and default stats" in {
      val player = Player("1")
      player.id should be("1")
      player.stats should be(Stats(0, 0, 3))
    }

    "take damage and reduce health" in {
      val player = Player("1")
      val updatedPlayer = player.takeDamage
      updatedPlayer.stats.health should be(2)
    }

    "notify OnPlayerDeathEvent when health is zero or less" in {
      var eventNotified = false
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          event match {
            case GameEvent.OnPlayerDeathEvent(_) => eventNotified = true
            case _                               => // do nothing
          }
        }
      }
      EventManager.subscribe(listener)

      val player = Player("1", Stats(0, 0, 1))
      val updatedPlayer = player.takeDamage
      updatedPlayer.stats.health should be(0)
      eventNotified should be(true)
    }

    "not notify OnPlayerDeathEvent when health is above zero" in {
      var eventNotified = false
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          event match {
            case GameEvent.OnPlayerDeathEvent(_) => eventNotified = true
            case _                               => // do nothing
          }
        }
      }
      EventManager.subscribe(listener)

      val player = Player("1", Stats(0, 0, 2))
      val updatedPlayer = player.takeDamage
      updatedPlayer.stats.health should be(1)
      eventNotified should be(false)
    }
  }
}
