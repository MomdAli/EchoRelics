package controller

import service.GameManager
import model.events.{EventManager, GameEvent}
import model.commands.Command
import model.events.{EventListener, EventManager, GameEvent}

class Controller(var gameManager: GameManager = GameManager.StartingManager)
    extends EventListener {

  EventManager.subscribe(this)

  override def handleEvent(event: GameEvent): Unit = {
    event match {
      case GameEvent.OnRelicCollectEvent(player, relic) =>
        gameManager = gameManager.collectRelic(player, relic)
        EventManager.notify(gameManager.event)
      case _ =>
    }
  }

  def handleCommand(command: Command): Boolean = {
    if (gameManager.isValid(command)) {
      gameManager = command.execute(gameManager)
      if (gameManager.event == GameEvent.OnQuitEvent) {
        return false
      }

      EventManager.notify(gameManager.event)
      EventManager.processEvents()
    }
    true
  }
}
