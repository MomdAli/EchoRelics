package service

import model.{Grid, Player, GameState}
import model.events.GameEvent
import utils.Command
import model.config.Config

case class MenuManager(
    move: Int,
    players: List[Player],
    grid: Grid,
    config: Config,
    event: GameEvent
) extends GameManager {

  val state: GameState = GameState.NotStarted

  def handleCommand(command: Command): GameManager = {
    command match {
      case Command.SetGridSize =>
        GridSizeManager(
          move,
          players,
          grid,
          config,
          GameEvent.OnSetGridSizeEvent(grid.size)
        )
      case Command.SetPlayerSize =>
        PlayerSizeManager(
          move,
          players,
          grid,
          config,
          GameEvent.OnSetPlayerSizeEvent(players.size)
        )
      case Command.StartGame =>
        val newConfig = Config.configurator(players.size, grid.size, 0)
        RunningManager(
          0,
          players,
          grid.setupGrid(players, newConfig.wallRatio),
          newConfig,
          GameEvent.OnGameStartEvent
        )
      case Command.Quit =>
        copy(event = GameEvent.OnQuitEvent)
      case _ =>
        copy(event = GameEvent.OnNoneEvent)
    }
  }
}
