package service.serviceImpl

import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import model.events.GameEvent
import model.{IGrid, ICommand, ITile}
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

  private def checkWinningCondition(newPlayers: List[IEntity]): Boolean = {
    newPlayers.exists(_.score >= 1000) || newPlayers.count(
      _.stats.health > 0
    ) == 1
  }

  private def getWinner(newPlayers: List[IEntity]): IEntity = {
    newPlayers
      .find(_.score >= 1000)
      .getOrElse(newPlayers.find(_.stats.health > 0).get)
  }

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

      val alivePlayers = players.filter(_.stats.health > 0)

      if (checkWinningCondition(alivePlayers))
        return WinnerManager(
          move,
          players,
          newGrid,
          GameEvent.OnWinnerEvent(getWinner(alivePlayers).id)
        )

      val newEvent =
        if (move % (config.relicSpawnRate * players.size) == 0 && move != 0)
          GameEvent.OnRelicSpawnEvent
        else GameEvent.OnPlayerMoveEvent

      change(
        move = move + 1,
        players = alivePlayers,
        grid = newGrid,
        event = newEvent
      )
    }
  }

  override def moveAllEchoes: IGameManager = {
    val newGrid = grid.moveEchoes
    copy(
      grid = newGrid,
      event = GameEvent.OnEchoesMoveEvent
    )
  }

  override def damagePlayer(player: IEntity): IGameManager = {
    val newPlayers = players.map { p =>
      if (p.id == player.id)
        p.updateStats(
          p.stats.decreaseHealth(1)
        )
      else p
    }

    val damagedPlayer = newPlayers.find(_.id == player.id).get

    println(newPlayers)

    if (damagedPlayer.stats.health == 0) {
      val alivePlayers = newPlayers.filter(_.stats.health > 0)
      val newGrid =
        grid.set(grid.findPlayer(damagedPlayer).get, ITile.emptyTile)

      if (checkWinningCondition(alivePlayers)) {
        WinnerManager(
          move,
          alivePlayers,
          newGrid,
          GameEvent.OnWinnerEvent(getWinner(alivePlayers).id)
        )
      } else {
        copy(
          players = alivePlayers,
          grid = newGrid,
          event = GameEvent.OnPlayerDeathEvent(damagedPlayer)
        )
      }
    } else {
      copy(
        players = newPlayers,
        event = GameEvent.OnUpdateRenderEvent
      )
    }
  }

  override def echo: IGameManager = {
    currentPlayer.stats.echoes match {
      case echoes if echoes > 0 => {
        val newGrid = grid.spawnEcho(
          grid.findPlayer(currentPlayer).get,
          injector
            .instance[IEntity](Names.named("Echo"))
            .withOwner(currentPlayer.id)
        )
        if (newGrid == grid) copy(event = GameEvent.OnNoneEvent)
        else
          copy(
            players = players.map { p =>
              if (p == currentPlayer)
                p.updateStats(
                  p.stats.decreaseEchoes(1)
                )
              else p
            },
            move = move + 1,
            grid = newGrid,
            event = GameEvent.OnEchoSpawnEvent
          )
      }
      case _ => copy(event = GameEvent.OnNoneEvent)
    }
  }

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
    val gotEcho = relic.obtainEcho

    def updatePlayerScore(player: IEntity, score: Int): IEntity = {
      player.updateScore(score + player.score)
    }

    def updatePlayers(updatedPlayer: IEntity): List[IEntity] = {
      players.map { p =>
        if (p == player) updatedPlayer else p
      }
    }

    val playerWithScore = updatePlayerScore(player, relicScore)

    val updatedPlayer = relicCard match {
      case Some(card) if !playerWithScore.inventory.isFull =>
        playerWithScore.inventory.addCard(card)
        playerWithScore
      case Some(card) =>
        val cardScore = card.toScore
        updatePlayerScore(playerWithScore, cardScore)
      case None =>
        playerWithScore
    }

    // Apply echo if obtained
    val finalPlayer = if (gotEcho) {
      updatedPlayer.updateStats(updatedPlayer.stats.increaseEchoes(1))
    } else {
      updatedPlayer
    }

    val updatedPlayers = updatePlayers(finalPlayer)

    val newEvent = (relicCard, gotEcho) match {
      case (Some(card), true) if !player.inventory.isFull =>
        GameEvent.OnInfoEvent(
          s"Player ${player.id} got $relicScore score, collected ${card.name} and obtained an Echo!"
        )
      case (Some(card), false) if !player.inventory.isFull =>
        GameEvent.OnInfoEvent(
          s"Player ${player.id} got $relicScore score and collected ${card.name}."
        )
      case (Some(card), echo) =>
        val cardScore = card.toScore
        GameEvent.OnInfoEvent(
          s"Player ${player.id} got $relicScore score and because the inventory was full, " +
            s"the card turned into $cardScore score" +
            (if (echo) " and obtained an Echo!" else ".")
        )
      case (None, true) =>
        GameEvent.OnInfoEvent(
          s"Player ${player.id} got $relicScore score and obtained an Echo!"
        )
      case (None, false) =>
        GameEvent.OnInfoEvent(s"Player ${player.id} got $relicScore score.")
    }

    if (checkWinningCondition(updatedPlayers)) {
      WinnerManager(
        move,
        updatedPlayers,
        grid,
        GameEvent.OnWinnerEvent(getWinner(updatedPlayers).id)
      )
    } else {
      copy(players = updatedPlayers, event = newEvent)
    }
  }
}
