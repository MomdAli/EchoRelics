package service

import model.{Grid, Player, GameState}
import model.events.GameEvent
import utils.Command
import model.config.Config

case class GridSizeManager(
    move: Int,
    players: List[Player],
    grid: Grid,
    config: Config,
    event: GameEvent
) extends GameManager {

  val state: GameState = GameState.NotStarted

  def handleCommand(command: Command): GameManager = {
    command match {
      case Command.MoveUp =>
        val newGrid = grid.increaseSize
        copy(
          grid = newGrid,
          event = GameEvent.OnSetGridSizeEvent(newGrid.size)
        )
      case Command.MoveDown =>
        val newGrid = grid.decreaseSize
        copy(
          grid = newGrid,
          event = GameEvent.OnSetGridSizeEvent(newGrid.size)
        )
      case Command.Quit =>
        MenuManager(
          move,
          players,
          grid,
          config,
          GameEvent.OnGameEndEvent
        )
      case _ =>
        copy(event = GameEvent.OnNoneEvent)
    }
  }
}
