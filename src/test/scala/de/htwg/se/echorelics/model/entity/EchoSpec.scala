package model.entity

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.events.{GameEvent, EventManager, EventListener}
import utils.Position

class EchoSpec extends AnyWordSpec with Matchers {

  "An Echo" should {

    "handle OnPlayerMoveEvent by notifying OnMoveEchoEvent" in {
      val player = Player("player1")
      val echo = Echo("echo1", player)
      var eventHandled = false

      val testListener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          event match {
            case GameEvent.OnMoveEchoEvent(e) if e == echo =>
              eventHandled = true
            case _ =>
          }
        }
      }

      EventManager.subscribe(testListener)
      EventManager.notify(GameEvent.OnPlayerMoveEvent)
      EventManager.processEvents()
      eventHandled should be(true)
    }

    "be walkable" in {
      val player = Player("player1")
      val echo = Echo("echo1", player)
      echo.isWalkable should be(true)
    }

    "not be collectable" in {
      val player = Player("player1")
      val echo = Echo("echo1", player)
      echo.isCollectable should be(false)
    }
  }
}
