package model.events

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EventManagerSpec extends AnyWordSpec with Matchers {

  "An EventManager" should {

    "subscribe a listener" in {
      val listener = new EventListener {
        def handleEvent(event: GameEvent): Unit = {}
      }
      EventManager.subscribe(listener)
      EventManager.isSubscribed(listener) should be(true)
    }

    "unsubscribe a listener" in {
      val listener = new EventListener {
        def handleEvent(event: GameEvent): Unit = {}
      }
      EventManager.subscribe(listener)
      EventManager.unsubscribe(listener)
      EventManager.isSubscribed(listener) should be(false)
    }

    "notify listeners instantly" in {
      var eventHandled = false
      val listener = new EventListener {
        def handleEvent(event: GameEvent): Unit = {
          eventHandled = true
        }
      }
      EventManager.subscribe(listener)
      EventManager.instantNotify(GameEvent.OnGameStartEvent)
      eventHandled should be(true)
    }

    "enqueue and process events" in {
      var eventHandled = false
      val listener = new EventListener {
        def handleEvent(event: GameEvent): Unit = {
          eventHandled = true
        }
      }
      EventManager.subscribe(listener)
      EventManager.notify(GameEvent.OnGameStartEvent)
      EventManager.processEvents()
      eventHandled should be(true)
    }

    "handle multiple events in order" in {
      var eventsHandled = List[GameEvent]()
      val listener = new EventListener {
        def handleEvent(event: GameEvent): Unit = {
          eventsHandled = eventsHandled :+ event
        }
      }
      EventManager.subscribe(listener)
      EventManager.notify(GameEvent.OnGameStartEvent)
      EventManager.notify(GameEvent.OnGameEndEvent)
      EventManager.processEvents()
      eventsHandled should be(
        List(GameEvent.OnGameStartEvent, GameEvent.OnGameEndEvent)
      )
    }
  }
}
