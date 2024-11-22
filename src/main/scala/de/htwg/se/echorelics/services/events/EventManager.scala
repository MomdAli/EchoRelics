package services.events

trait EventListener {
  def handleEvent(event: GameEvent): Unit
}

class EventManager {
  private var listeners: List[EventListener] = List()

  def add(listener: EventListener): Unit = {
    listeners = listener :: listeners
  }

  def remove(listener: EventListener): Unit = {
    listeners = listeners.filter(_ != listener)
  }

  def notifyListeners(event: GameEvent): Unit = {
    listeners.foreach(_.handleEvent(event))
  }
}
