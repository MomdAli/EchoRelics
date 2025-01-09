package service.serviceImpl

import model.IGrid
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

  override def quit: IGameManager = {
    MenuManager(
      move,
      players,
      grid,
      event = GameEvent.OnGameEndEvent
    )
  }
}
