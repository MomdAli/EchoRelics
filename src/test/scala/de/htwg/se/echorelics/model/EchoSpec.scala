package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.events.{EventManager, GameEvent, EventListener}

class EchoSpec extends AnyWordSpec with Matchers {

  "An Echo" should {

    "be created with a given id and owner" in {
      val player = Player("1")
      val echo = Echo("echo1", player)
      echo.id should be("echo1")
      echo.owner should be(player)
    }

    "subscribe to the EventManager on creation" in {
      var eventNotified = false
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          event match {
            case GameEvent.OnMoveEchoEvent(_) => eventNotified = true
            case _                            => // do nothing
          }
        }
      }
      EventManager.subscribe(listener)

      val player = Player("1")
      val echo = Echo("echo1", player)
      EventManager.notify(GameEvent.OnPlayerMoveEvent)
      eventNotified should be(true)
    }

    "notify OnMoveEchoEvent when a player moves" in {
      var eventNotified = false
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          event match {
            case GameEvent.OnMoveEchoEvent(_) => eventNotified = true
            case _                            => // do nothing
          }
        }
      }
      EventManager.subscribe(listener)

      val player = Player("1")
      val echo = Echo("echo1", player)
      EventManager.notify(GameEvent.OnPlayerMoveEvent)
      eventNotified should be(true)
    }

    "print a message when the owner player dies" in {
      val player = Player("1")
      val echo = Echo("echo1", player)

      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        EventManager.notify(GameEvent.OnPlayerDeathEvent(player))
      }
      stream.toString should include("Echo: Player died")
    }
  }
}
