package service.serviceImpl

import model.entity.IEntity
import model.IGrid
import model.events.GameEvent
import model.item.IInventory
import service.IGameManager
import utils.GameState

case class MenuManager(
    move: Int,
    players: List[IEntity],
    grid: IGrid,
    event: GameEvent
) extends IGameManager {

  val state: GameState = GameState.NotStarted

  override def setGridSize: IGameManager = {
    GridSizeManager(
      move,
      players,
      grid,
      GameEvent.OnSetGridSizeEvent(grid.size)
    )
  }

  override def setPlayerSize: IGameManager = {
    PlayerSizeManager(
      move,
      players,
      grid,
      GameEvent.OnSetPlayerSizeEvent(players.size)
    )
  }

  override def start: IGameManager = {
    RunningManager(
      0, // Starting the game with 0 moves
      players.map(player => IEntity.createPlayer(player.id)),
      gridSpawner.setupStartingGrid(grid, players),
      GameEvent.OnGameStartEvent
    )
  }

  override def quit: IGameManager = {
    copy(event = GameEvent.OnQuitEvent)
  }
}
