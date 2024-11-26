package model

import utils.Position
import model.events.{EventManager, EventListener, GameEvent}

case class Echo(
    id: String,
    owner: Player
) extends EventListener {
  EventManager.subscribe(this)

  override def handleEvent(event: GameEvent): Unit = {
    event match {
      case GameEvent.OnPlayerMoveEvent =>
        EventManager.notify(GameEvent.OnMoveEchoEvent(this))
      case GameEvent.OnPlayerDeathEvent(player) =>
        println("Echo: Player died")
      case _ =>
    }
  }
}
