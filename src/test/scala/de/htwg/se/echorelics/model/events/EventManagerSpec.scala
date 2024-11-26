package model.events

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EventManagerSpec extends AnyWordSpec with Matchers {

  "An EventManager" should {

    "allow listeners to subscribe" in {
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {}
      }
      EventManager.subscribe(listener)
      // Instead of directly accessing the private variable, we can test the behavior
      // by checking if the listener is notified when an event is triggered.
      var eventHandled = false
      val testListener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          eventHandled = true
        }
      }
      EventManager.subscribe(testListener)
      EventManager.notify(GameEvent.OnGameStartEvent)
      eventHandled should be(true)
    }

    "allow listeners to unsubscribe" in {
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {}
      }
      EventManager.subscribe(listener)
      EventManager.unsubscribe(listener)
      // Instead of directly accessing the private variable, we can test the behavior
      // by checking if the listener is not notified when an event is triggered.
      var eventHandled = false
      val testListener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          eventHandled = true
        }
      }
      EventManager.subscribe(testListener)
      EventManager.unsubscribe(testListener)
      EventManager.notify(GameEvent.OnGameStartEvent)
      eventHandled should be(false)
    }

    "notify all subscribed listeners of an event" in {
      var eventHandled = false
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          eventHandled = true
        }
      }
      EventManager.subscribe(listener)
      EventManager.notify(GameEvent.OnGameStartEvent)
      eventHandled should be(true)
    }

    "not notify unsubscribed listeners of an event" in {
      var eventHandled = false
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          eventHandled = true
        }
      }
      EventManager.subscribe(listener)
      EventManager.unsubscribe(listener)
      EventManager.notify(GameEvent.OnGameStartEvent)
      eventHandled should be(false)
    }
  }
}
