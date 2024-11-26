package model.events

trait EventListener {
  def handleEvent(event: GameEvent): Unit
}

object EventManager {
  private var listeners: List[EventListener] = List()

  def subscribe(listener: EventListener): Unit = {
    listeners = listener :: listeners
  }

  def unsubscribe(listener: EventListener): Unit = {
    listeners = listeners.filter(_ != listener)
  }

  def notify(event: GameEvent): Unit = {
    listeners.foreach(_.handleEvent(event))
  }

  def isSubscribed(listener: EventListener): Boolean = {
    listeners.contains(listener)
  }
}
