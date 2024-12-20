package service

import model.entity.Player
import model.{Grid, GameState}
import model.events.GameEvent
import model.commands.{Command, MoveCommand, QuitCommand}
import utils.Direction

case class PlayerSizeManager(
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

  override def move(direction: Direction): GameManager = {
    direction match {
      case Direction.Up   => increasePlayerSize
      case Direction.Down => decreasePlayerSize
      case _              => this
    }
  }

  override def quit: GameManager = {
    MenuManager(move, players, grid, GameEvent.OnGameEndEvent)
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
      if (players.size > 1) players.init
      else players

    copy(
      players = newPlayers,
      event = GameEvent.OnSetPlayerSizeEvent(newPlayers.size)
    )
  }
}
