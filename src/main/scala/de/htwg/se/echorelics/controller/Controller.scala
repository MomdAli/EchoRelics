package controller

import scala.util.{Try, Success, Failure}

import model.ICommand
import model.events.{EventManager, EventListener, GameEvent}
import service.IGameManager

class Controller(var gameManager: IGameManager = IGameManager())
    extends EventListener {

  EventManager.subscribe(this)

  val commandHistory: ICommand = ICommand.createCommandHistory()

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

  def handleCommand(command: ICommand): Try[IGameManager] = Try {
    command.execute(gameManager) match {
      case Success(updatedGameManager) =>
        gameManager = updatedGameManager
        commandHistory.execute(gameManager)
        EventManager.notify(gameManager.event)
        EventManager.notify(GameEvent.OnUpdateRenderEvent)
        EventManager.processEvents()
        gameManager
      case Failure(exception) =>
        gameManager
    }
  }

  def undo(turns: Int): Try[IGameManager] = {
    (0 until turns).foldLeft(Try(gameManager)) { (result, _) =>
      result.flatMap { gm =>
        commandHistory.undo(gm) match {
          case Success(updatedGameManager: IGameManager) =>
            EventManager.notify(GameEvent.OnUpdateRenderEvent)
            Success(updatedGameManager)
          case Failure(exception) =>
            Failure(exception)
        }
      }
    }
  }
}
