package model.events

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EventManagerSpec extends AnyWordSpec with Matchers {

  "An EventManager" should {

    "subscribe a listener" in {
      val listener = new TestListener
      EventManager.subscribe(listener)
      EventManager.isSubscribed(listener) should be(true)
    }

    "unsubscribe a listener" in {
      val listener = new TestListener
      EventManager.subscribe(listener)
      EventManager.unsubscribe(listener)
      EventManager.isSubscribed(listener) should be(false)
    }

    "notify listeners instantly" in {
      val listener = new TestListener
      EventManager.subscribe(listener)
      val event = GameEvent.OnInfoEvent("Test")
      EventManager.instantNotify(event)
      listener.handledEvents should contain(event)
    }

    "process events asynchronously" in {
      val listener = new TestListener
      EventManager.subscribe(listener)
      val event = GameEvent.OnInfoEvent("Test")
      EventManager.notify(event)
      Future {
        EventManager.processEvents()
      }
      Thread.sleep(100) // Wait for async processing
      listener.handledEvents should contain(event)
    }
  }

  class TestListener extends EventListener {
    var handledEvents: List[GameEvent] = List()

    override def handleEvent(event: GameEvent): Unit = {
      handledEvents = event :: handledEvents
    }
  }
}
