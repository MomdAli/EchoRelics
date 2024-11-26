package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.events.{EventManager, GameEvent}
import utils.Position
import model.events.EventListener

class EchoSpec extends AnyWordSpec with Matchers {

  "An Echo" should {

    "be created with a given id and owner" in {
      val player = Player("1")
      val echo = Echo("echo1", player)
      echo.id should be("echo1")
      echo.owner should be(player)
    }

    "subscribe to EventManager on creation" in {
      val player = Player("1")
      val echo = Echo("echo1", player)
      EventManager.isSubscribed(echo) should be(true)
    }

    "handle OnPlayerMoveEvent and notify OnMoveEchoEvent" in {
      val player = Player("1")
      val echo = Echo("echo1", player)
      var eventNotified = false
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          event match {
            case GameEvent.OnMoveEchoEvent(e) if e == echo =>
              eventNotified = true
            case _ => // do nothing
          }
        }
      }
      EventManager.subscribe(listener)
      EventManager.notify(GameEvent.OnPlayerMoveEvent)
      eventNotified should be(true)
    }

    "handle OnPlayerDeathEvent and print message" in {
      val player = Player("1")
      val echo = Echo("echo1", player)
      // Redirect console output to capture the print statement
      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        EventManager.notify(GameEvent.OnPlayerDeathEvent(player))
      }
      stream.toString should include("Echo: Player died")
    }
  }
}
