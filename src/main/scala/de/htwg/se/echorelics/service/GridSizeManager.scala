package service

import model.entity.Player
import model.{Grid, GameState}
import model.events.GameEvent
import model.commands.{Command, MoveCommand, QuitCommand}
import utils.Direction

case class GridSizeManager(
    move: Int,
    players: List[Player],
    grid: Grid,
    event: GameEvent
) extends GameManager {

  val state: GameState = GameState.NotStarted

  override def isValid(command: Command): Boolean = {
    command match {
      case MoveCommand(Direction.Up) | MoveCommand(Direction.Down) |
          QuitCommand() =>
        true
      case _ => false
    }
  }

  override def moveUp: GameManager = {
    val newGrid = grid.increaseSize
    copy(
      grid = newGrid,
      event = GameEvent.OnSetGridSizeEvent(newGrid.size)
    )
  }

  override def moveDown: GameManager = {
    val newGrid = grid.decreaseSize
    copy(
      grid = newGrid,
      event = GameEvent.OnSetGridSizeEvent(newGrid.size)
    )
  }

  override def quit: GameManager =
    MenuManager(
      move,
      players,
      grid,
      GameEvent.OnGameEndEvent
    )
}
