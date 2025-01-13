package model.entity.entityImpl

import com.google.inject.Inject

import model.entity.IEntity
import model.events.{GameEvent, EventListener, EventManager}
import utils.Position

case class Echo @Inject() (
    id: String,
    owner: Player
) extends EventListener
    with IEntity {
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

  override def isWalkable: Boolean = true
  override def isCollectable: Boolean = false
}
