package service.serviceImpl

import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import model.events.GameEvent
import model.{IGrid, ICommand}
import model.entity.IEntity
import utils.{Direction, GameState}
import model.item.ICard
import service.IGameManager

case class RunningManager(
    move: Int,
    players: List[IEntity],
    grid: IGrid,
    event: GameEvent
) extends IGameManager {

  override def state: GameState = GameState.Running

  override def move(direction: Direction): IGameManager = {
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

  override def echo: IGameManager = ???

  override def pause: IGameManager = {
    PausedManager(
      move,
      players,
      grid,
      GameEvent.OnGamePauseEvent
    )
  }

  override def quit: IGameManager = {
    echorelics.EchoRelics.controller.handleCommand(
      injector.instance[ICommand](Names.named("Save"))
    )
    MenuManager(
      move,
      players,
      injector.instance[IGrid](Names.named("Small")),
      GameEvent.OnGameEndEvent
    )
  }

  override def playerCard(index: Int): Option[ICard] = {
    currentPlayer.inventory.cardAt(index)
  }

  override def spawnRelic: IGameManager = {
    copy(
      grid = gridSpawner.spawnRelic(grid),
      event = GameEvent.OnInfoEvent("Relics spawned!")
    )
  }

  override def collectRelic(player: IEntity, relic: IEntity): IGameManager = {
    val relicScore = relic.score
    val relicCard = relic.collectCard

    def updatePlayerScore(player: IEntity, score: Int): IEntity = {
      player.updateScore(score)
    }

    def updatePlayers(updatedPlayer: IEntity): List[IEntity] = {
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
