package model.events

import scala.collection.mutable.Queue

object EventManager {
  private var listeners: List[EventListener] = List()
  private val eventQueue: Queue[GameEvent] = Queue()

  def subscribe(listener: EventListener): Unit = {
    listeners = listener :: listeners
  }

  def unsubscribe(listener: EventListener): Unit = {
    listeners = listeners.filter(_ != listener)
  }

  def notify(event: GameEvent): Unit = {
    eventQueue.enqueue(event)
  }

  def processEvents(): Unit = {
    while (eventQueue.nonEmpty) {
      val event = eventQueue.dequeue()
      listeners.foreach(_.handleEvent(event))
    }
  }

  def isSubscribed(listener: EventListener): Boolean = {
    listeners.contains(listener)
  }
}

trait EventListener {
  def handleEvent(event: GameEvent): Unit
}
