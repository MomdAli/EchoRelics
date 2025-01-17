package service.serviceImpl

import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import model.{IGrid, ICommand}
import model.config.Config
import model.entity.IEntity
import model.events.GameEvent
import service.IGameManager
import utils.GameState

case class PausedManager(
    move: Int,
    players: List[IEntity],
    grid: IGrid,
    event: GameEvent
) extends IGameManager {

  val state: GameState = GameState.Paused

  override def resume: IGameManager = {
    RunningManager(move, players, grid, GameEvent.OnGameResumeEvent)
  }

  override def pause: IGameManager = {
    RunningManager(move, players, grid, GameEvent.OnGameResumeEvent)
  }

  override def quit: IGameManager = {
    echorelics.EchoRelics.controller.handleCommand(
      injector.instance[ICommand](Names.named("Save"))
    )
    MenuManager(
      move,
      players,
      grid,
      event = GameEvent.OnGameEndEvent
    )
  }
}
