package service

import utils.{Command, Direction}
import model.config.Config
import model.events.GameEvent
import model.{Grid, Player, GameState}
import model.TileContent
import model.Echo

case class RunningManager(
    move: Int,
    players: List[Player],
    grid: Grid,
    config: Config,
    event: GameEvent,
    override val echoes: List[Echo] = List(),
    confirmation: Boolean = false,
    echoSpawned: Boolean = false
) extends GameManager {

  override def state: GameState = GameState.Running

  override def handleCommand(command: Command): GameManager = {
    command match {
      case Command.MoveDown  => movePlayer(Direction.Down)
      case Command.MoveUp    => movePlayer(Direction.Up)
      case Command.MoveLeft  => movePlayer(Direction.Left)
      case Command.MoveRight => movePlayer(Direction.Right)
      case Command.SpawnEcho => spawnEcho
      case Command.PauseGame =>
        PausedManager(
          move,
          players,
          grid,
          config,
          GameEvent.OnGamePauseEvent
        )
      case Command.Quit =>
        if (confirmation) {
          MenuManager(
            move,
            players,
            new Grid(grid.size),
            config,
            GameEvent.OnGameEndEvent
          )
        } else {
          copy(
            event = GameEvent.OnErrorEvent(
              "Are you sure you want to quit? Press Q again to confirm."
            ),
            confirmation = true
          )
        }

      case _ =>
        copy(event = GameEvent.OnNoneEvent)

    }
  }

  private def movePlayer(direction: Direction): RunningManager = {
    val drop = if echoSpawned then TileContent.Echo else TileContent.Empty
    val newGrid = grid.movePlayer(currentPlayer, direction, drop)
    if (newGrid == grid) {
      copy(
        event = GameEvent.OnErrorEvent(
          "Cannot move player there! Try again."
        ),
        confirmation = false
      )
    } else {
      copy(
        move = move + 1,
        grid = newGrid,
        event =
          if round % config.relicSpawnRate == 0 then GameEvent.OnRelicSpawnEvent
          else GameEvent.OnPlayerMoveEvent,
        echoSpawned = false,
        confirmation = false
      )
    }
  }

  private def spawnEcho: RunningManager = {
    copy(
      echoes = Echo("e", currentPlayer) :: echoes,
      event = GameEvent.OnEchoSpawnEvent,
      echoSpawned = true
    )
  }

  override def handleEchoMove(echo: Echo): RunningManager = {
    val newGrid = grid.moveEcho(echo)
    copy(
      grid = newGrid,
      event = GameEvent.OnStealRelicEvent(echo)
    )
  }
}
