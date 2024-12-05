package controller

import scala.util.{Try, Success, Failure}

import service.GameManager
import model.events.{EventManager, GameEvent}
import model.commands.Command
import model.events.{EventListener, EventManager, GameEvent}
import model.commands.CommandHistory

class Controller(var gameManager: GameManager = GameManager())
    extends EventListener {

  EventManager.subscribe(this)

  val commandHistory: CommandHistory = new CommandHistory()

  override def handleEvent(event: GameEvent): Unit = {
    event match {
      case GameEvent.OnRelicCollectEvent(player, relic) =>
        gameManager = gameManager.collectRelic(player, relic)
        EventManager.notify(GameEvent.OnUpdateRenderEvent)
      case GameEvent.OnRelicSpawnEvent =>
        gameManager = gameManager.spawnRelic
        EventManager.notify(GameEvent.OnUpdateRenderEvent)
        EventManager.notify(GameEvent.OnInfoEvent("Relic spawned!"))
      case GameEvent.OnTimeTravelEvent(turns) =>
        gameManager = commandHistory.undo(gameManager, turns)
        EventManager.notify(GameEvent.OnUpdateRenderEvent)
        EventManager.notify(
          GameEvent.OnInfoEvent("Player activated time travel!")
        )
      case _ =>
    }
  }

  def handleCommand(command: Command): Try[GameManager] = Try {
    if (!gameManager.isValid(command)) {
      gameManager
    } else {
      command.execute(gameManager) match {
        case Success(updatedGameManager) =>
          gameManager = updatedGameManager
          commandHistory.add(command)
          EventManager.notify(gameManager.event)
          gameManager
        case Failure(exception) =>
          gameManager
      }
    }
  }
}
