package service

import model.{Grid, Player, GameState}
import utils.Command
import model.config.Config
import model.events.GameEvent

case class PausedManager(
    move: Int,
    players: List[Player],
    grid: Grid,
    config: Config,
    event: GameEvent
) extends GameManager {

  val state: GameState = GameState.Paused

  def handleCommand(command: Command): GameManager = {
    command match {
      case Command.ResumeGame =>
        RunningManager(move, players, grid, config, GameEvent.OnGameResumeEvent)
      case Command.Quit =>
        MenuManager(
          move,
          players,
          new Grid(0),
          config,
          GameEvent.OnGameEndEvent
        )
      case _ =>
        copy(event =
          GameEvent.OnErrorEvent("Game is Paused. Press 'r' to resume.")
        )
    }
  }
}
