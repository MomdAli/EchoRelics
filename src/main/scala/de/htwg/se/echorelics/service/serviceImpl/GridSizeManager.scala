package service.serviceImpl

import model.entity.IEntity
import model.IGrid
import model.events.GameEvent
import service.IGameManager
import utils.{Direction, GameState}

case class GridSizeManager(
    move: Int,
    players: List[IEntity],
    grid: IGrid,
    event: GameEvent
) extends IGameManager {

  val state: GameState = GameState.NotStarted

  override def start: IGameManager =
    RunningManager(
      0, // Starting the game with 0 moves
      players.map(player => {
        playerProvider.setId(player.id)
        playerProvider.get()
      }),
      gridSpawner.setupStartingGrid(grid, players),
      GameEvent.OnGameStartEvent
    )

  override def move(direction: Direction): IGameManager = {
    direction match {
      case Direction.Up => {
        val newGrid = grid.increaseSize
        copy(
          grid = newGrid,
          event = GameEvent.OnSetGridSizeEvent(newGrid.size)
        )
      }
      case Direction.Down => {
        val newGrid = grid.decreaseSize
        copy(
          grid = newGrid,
          event = GameEvent.OnSetGridSizeEvent(newGrid.size)
        )
      }
      case _ => this
    }
  }

  override def quit: IGameManager =
    MenuManager(
      move,
      players,
      grid,
      GameEvent.OnGameEndEvent
    )
}
