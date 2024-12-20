package service

import model.{Grid, GameState}
import model.config.Config
import model.entity.Player
import model.events.GameEvent
import model.commands.{Command, ResumeCommand, QuitCommand}

case class PausedManager(
    move: Int,
    players: List[Player],
    grid: Grid,
    event: GameEvent
) extends GameManager {

  val state: GameState = GameState.Paused

  override def isValid(command: Command): Boolean = {
    command match {
      case ResumeCommand() | QuitCommand() => true
      case _                               => false
    }
  }

  override def resume: GameManager = {
    RunningManager(move, players, grid, GameEvent.OnGameResumeEvent)
  }

  override def quit: GameManager = {
    MenuManager(
      move,
      players,
      grid,
      event = GameEvent.OnGameEndEvent
    )
  }
}
