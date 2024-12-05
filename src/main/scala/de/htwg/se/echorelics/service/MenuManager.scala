package service

import model.entity.Player
import model.{Grid, GameState}
import model.events.GameEvent
import model.commands.{
  Command,
  GridSizeCommand,
  PlayerSizeCommand,
  StartCommand,
  QuitCommand
}
import model.item.Inventory

case class MenuManager(
    move: Int,
    players: List[Player],
    grid: Grid,
    event: GameEvent
) extends GameManager {

  val state: GameState = GameState.NotStarted

  override def isValid(command: Command): Boolean = {
    command match {
      case GridSizeCommand() | PlayerSizeCommand() | StartCommand() |
          QuitCommand() =>
        true
      case _ => false
    }
  }

  override def setGridSize: GameManager = {
    GridSizeManager(
      move,
      players,
      grid,
      GameEvent.OnSetGridSizeEvent(grid.size)
    )
  }

  override def setPlayerSize: GameManager = {
    PlayerSizeManager(
      move,
      players,
      grid,
      GameEvent.OnSetPlayerSizeEvent(players.size)
    )
  }

  override def start: GameManager = {
    RunningManager(
      0, // Starting the game with 0 moves
      players.map(player => Player(player.id)),
      gridSpawner.setupStartingGrid(grid, players),
      GameEvent.OnGameStartEvent
    )
  }

  override def quit: GameManager = {
    copy(event = GameEvent.OnQuitEvent)
  }
}
