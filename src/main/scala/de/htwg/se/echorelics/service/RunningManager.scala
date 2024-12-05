package service

import model.events.GameEvent
import model.{Grid, GameState}
import model.entity.{Echo, Player, Relic}
import utils.Direction
import model.commands.{
  Command,
  EchoCommand,
  QuitCommand,
  PauseCommand,
  MoveCommand,
  PlayCardCommand
}
import model.item.Card

case class RunningManager(
    move: Int,
    players: List[Player],
    grid: Grid,
    event: GameEvent
) extends GameManager {

  override def state: GameState = GameState.Running

  override def isValid(command: Command): Boolean = {
    command match {
      case MoveCommand(_) | PauseCommand() | QuitCommand() | EchoCommand() |
          PlayCardCommand(_) =>
        true
      case _ => false
    }
  }

  override def move(direction: Direction): GameManager = {
    val newGrid = grid.movePlayer(currentPlayer, direction)
    if (newGrid == grid) {
      copy(
        event = GameEvent.OnErrorEvent(
          "Cannot move player there! Try again."
        )
      )
    } else {
      val newEvent =
        if (move % config.relicSpawnRate == 0 && move != 0)
          GameEvent.OnRelicSpawnEvent
        else GameEvent.OnPlayerMoveEvent

      copy(
        move = move + 1,
        grid = newGrid,
        event = newEvent
      )
    }
  }

  override def echo: GameManager = ???

  override def pause: GameManager = {
    PausedManager(
      move,
      players,
      grid,
      GameEvent.OnGamePauseEvent
    )
  }

  override def quit: GameManager = {
    MenuManager(
      move,
      players,
      new Grid(grid.size),
      GameEvent.OnGameEndEvent
    )
  }

  override def playerCard(index: Int): Option[Card] = {
    currentPlayer.inventory.cardAt(index)
  }

  override def spawnRelic: GameManager = {
    copy(
      grid = gridSpawner.spawnRelic(grid),
      event = GameEvent.OnInfoEvent("Relics spawned!")
    )
  }

  override def collectRelic(player: Player, relic: Relic): GameManager = {
    val relicScore = relic.score
    val relicCard = relic.collectCard

    def updatePlayerScore(player: Player, score: Int): Player = {
      player.copy(stats = player.stats.updateScore(score))
    }

    def updatePlayers(updatedPlayer: Player): List[Player] = {
      players.map { p =>
        if (p == player) updatedPlayer else p
      }
    }

    val updatedPlayer = relicCard match {
      case Some(card) if !player.inventory.isFull =>
        player.inventory.addCard(card)
        updatePlayerScore(player, relicScore)
      case Some(card) =>
        val cardScore = card.toScore
        updatePlayerScore(player, relicScore + cardScore)
      case None =>
        updatePlayerScore(player, relicScore)
    }

    val updatedPlayers = updatePlayers(updatedPlayer)

    val newEvent = relicCard match {
      case Some(card) =>
        if (!player.inventory.isFull) {
          GameEvent.OnInfoEvent(
            s"Player ${player.id} got $relicScore score and collected ${card.name}."
          )
        } else {
          val cardScore = card.toScore
          GameEvent.OnInfoEvent(
            s"Player ${player.id} got $relicScore score and because the inventory was full," +
              s" the card turned into $cardScore score."
          )
        }
      case None =>
        GameEvent.OnInfoEvent(s"Player ${player.id} got $relicScore score.")
    }

    copy(players = updatedPlayers, event = newEvent)
  }
}
