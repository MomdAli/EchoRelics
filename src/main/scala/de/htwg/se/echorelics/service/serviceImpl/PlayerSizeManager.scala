package service.serviceImpl

import model.entity.IEntity
import model.IGrid
import model.events.GameEvent
import service.IGameManager
import utils.{Direction, GameState}

case class PlayerSizeManager(
    move: Int,
    players: List[IEntity],
    grid: IGrid,
    event: GameEvent
) extends IGameManager {

  val state: GameState = GameState.NotStarted

  override def move(direction: Direction): IGameManager = {
    direction match {
      case Direction.Up   => increasePlayerSize
      case Direction.Down => decreasePlayerSize
      case _              => this
    }
  }

  override def start: IGameManager = {
    GridSizeManager(
      move,
      players,
      grid,
      GameEvent.OnSetGridSizeEvent(grid.size)
    )
  }

  override def quit: IGameManager = {
    MenuManager(move, players, grid, GameEvent.OnGameEndEvent)
  }

  private def increasePlayerSize: PlayerSizeManager = {
    val newPlayers =
      if (players.size < 4) {
        playerProvider.setId((players.size + 1).toString)
        val newPlayer = playerProvider.get()
        players :+ newPlayer
      } else players

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
