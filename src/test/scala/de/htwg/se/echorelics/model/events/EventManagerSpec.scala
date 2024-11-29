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
      EventManager.isSubscribed(listener) should be(true)
    }

    "allow listeners to unsubscribe" in {
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {}
      }
      EventManager.subscribe(listener)
      EventManager.unsubscribe(listener)
      EventManager.isSubscribed(listener) should be(false)
    }

    "notify listeners of events" in {
      var eventHandled = false
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          eventHandled = true
        }
      }
      EventManager.subscribe(listener)
      EventManager.notify(GameEvent.OnInfoEvent("Test"))
      EventManager.processEvents()
      eventHandled should be(true)
    }

    "process multiple events in order" in {
      var eventCount = 0
      val listener = new EventListener {
        override def handleEvent(event: GameEvent): Unit = {
          eventCount += 1
        }
      }
      EventManager.subscribe(listener)
      EventManager.notify(GameEvent.OnInfoEvent("Test 1"))
      EventManager.notify(GameEvent.OnInfoEvent("Test 2"))
      EventManager.processEvents()
      eventCount should be(2)
    }
  }
}
