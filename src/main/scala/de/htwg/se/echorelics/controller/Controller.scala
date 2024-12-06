package controller

import scala.util.{Try, Success, Failure}

import model.commands.{Command, CommandHistory}
import model.events.{EventManager, EventListener, GameEvent}
import service.GameManager

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
        undo(turns) match {
          case Success(updatedGameManager) =>
            gameManager = updatedGameManager
            EventManager.notify(GameEvent.OnUpdateRenderEvent)
          case Failure(exception) =>
        }
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
          commandHistory.saveState(gameManager)
          EventManager.notify(gameManager.event)
          gameManager
        case Failure(exception) =>
          gameManager
      }
    }
  }

  def undo(turns: Int): Try[GameManager] = {
    (0 until turns).foldLeft(Try(gameManager)) { (result, _) =>
      result.flatMap { gm =>
        commandHistory.undo(gm) match {
          case Some(updatedGameManager) =>
            EventManager.notify(GameEvent.OnUpdateRenderEvent)
            Success(updatedGameManager)
          case None =>
            Failure(new Exception("No actions to undo."))
        }
      }
    }
  }
}
