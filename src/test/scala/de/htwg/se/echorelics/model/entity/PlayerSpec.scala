package model.entity

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.events.{GameEvent, EventManager, EventListener}
import model.Stats
import model.item.Inventory

class PlayerSpec extends AnyWordSpec with Matchers {

  "A Player" should {

    "have default stats and inventory" in {
      val player = Player("player1")
      player.stats should be(Stats(0, 0, 3))
      player.inventory should be(Inventory())
    }

    "take damage and notify OnPlayerDeathEvent when health is 0" in {
      val player = Player("player1", Stats(0, 0, 1))
      var eventHandled = false

      val testListener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          event match {
            case GameEvent.OnPlayerDeathEvent(p) if p == player =>
              eventHandled = true
            case _ =>
          }
        }
      }

      EventManager.subscribe(testListener)
      val updatedPlayer = player.takeDamage
      EventManager.processEvents()
      updatedPlayer.stats.health should be(0)
      eventHandled should not be (true)
    }

    "heal and increase health" in {
      val player = Player("player1", Stats(0, 0, 2))
      val healedPlayer = player.heal
      healedPlayer.stats.health should be(3)
    }

    "not be walkable" in {
      val player = Player("player1")
      player.isWalkable should be(false)
    }

    "not be collectable" in {
      val player = Player("player1")
      player.isCollectable should be(false)
    }
  }
}
