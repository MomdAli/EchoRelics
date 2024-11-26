package service

import model.{Grid, Player, GameState}
import model.events.GameEvent
import model.config.Config
import utils.Command

case class PlayerSizeManager(
    move: Int,
    players: List[Player],
    grid: Grid,
    config: Config,
    event: GameEvent
) extends GameManager {

  val state: GameState = GameState.NotStarted

  def handleCommand(command: Command): GameManager = {
    command match {
      case Command.MoveUp   => increasePlayerSize
      case Command.MoveDown => decreasePlayerSize
      case Command.Quit =>
        MenuManager(move, players, grid, config, GameEvent.OnGameEndEvent)
      case _ =>
        PlayerSizeManager(move, players, grid, config, GameEvent.OnNoneEvent)
    }
  }

  private def increasePlayerSize: PlayerSizeManager = {
    val newPlayers =
      if (players.size < 4) players :+ Player((players.size + 1).toString)
      else players

    copy(
      players = newPlayers,
      event = GameEvent.OnSetPlayerSizeEvent(newPlayers.size)
    )
  }

  private def decreasePlayerSize: PlayerSizeManager = {
    val newPlayers =
      if (players.size > 2) players.init
      else players

    copy(
      players = newPlayers,
      event = GameEvent.OnSetPlayerSizeEvent(newPlayers.size)
    )
  }
}
