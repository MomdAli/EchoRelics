package controller

import scala.util.{Try, Success, Failure}
import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import model.ICommand
import model.events.{EventManager, EventListener, GameEvent}
import modules.EchorelicsModule
import service.IGameManager

class Controller(implicit var gameManager: IGameManager) extends EventListener {

  EventManager.subscribe(this)

  val injector = Guice.createInjector(new EchorelicsModule)

  val commandHistory: ICommand =
    injector.instance[ICommand](Names.named("History"))

  override def handleEvent(event: GameEvent): Unit = {
    event match {
      case GameEvent.OnPlayerMoveEvent =>
        gameManager = gameManager.moveAllEchoes
        EventManager.notify(gameManager.event)
      case GameEvent.OnRelicCollectEvent(player, relic) =>
        gameManager = gameManager.collectRelic(player, relic)
        EventManager.notify(gameManager.event)
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
      case GameEvent.OnPlayerDamageEvent(player) =>
        gameManager = gameManager.damagePlayer(player)
        EventManager.notify(gameManager.event)
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

  def loadSave(manager: IGameManager): Unit = {
    gameManager = manager
    EventManager.notify(GameEvent.OnUpdateRenderEvent)
  }
}
