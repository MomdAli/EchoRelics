package services

import model.{GameState, Grid, Player}
import utils.{Direction, InputHandler, Position}
import view.DisplayRenderer

case class GameManager(
    val move: Int = 0,
    val players: List[Player] = List(),
    val state: GameState = GameState.NotStarted,
    val grid: Grid = new Grid(10)
) {

  def isGameStarted: Boolean = state != GameState.NotStarted

  def currentPlayer: Player = {
    players.size match {
      case 0 => Player("0")
      case _ => players(move % players.size)
    }
  }

  def setGrid(newGrid: Grid): GameManager = {
    if (state == GameState.NotStarted) {
      new GameManager(move, players, state, newGrid)
    } else {
      this
    }
  }

  def spawnEcho(): GameManager = {
    state match {
      case GameState.Running =>
        val newGrid = grid.spawnEcho(currentPlayer)
        if (newGrid == grid) {
          this
        } else {
          new GameManager(move + 1, players, state, newGrid)
        }

      case _ =>
        this
    }
  }

  def getState: GameState = state

  def moveNextPlayer(direction: Direction): GameManager = {
    state match {
      case GameState.Running =>
        val newGrid = grid.movePlayer(currentPlayer, direction)
        if (newGrid == grid) {
          this
        } else {
          new GameManager(move + 1, players, state, newGrid)
        }

      case _ =>
        this
    }
  }

  def addPlayer(player: Player): GameManager = {
    state match {
      case GameState.NotStarted =>
        new GameManager(move, players :+ player, state, grid)
      case _ =>
        this
    }
  }

  def getPlayer(id: String): Option[Player] = {
    players.find(_.id == id)
  }

  def getPlayerPosition(id: String): Position = {
    grid.findPlayer(getPlayer(id).get)
  }

  def removePlayer(player: Player): GameManager = {
    state match {
      case GameState.NotStarted =>
        new GameManager(move, players.filterNot(_.id == player.id), state, grid)
      case _ =>
        this
    }
  }

  def startGame(): GameManager = {
    state match {
      case GameState.NotStarted if players.nonEmpty =>
        new GameManager(
          move,
          players,
          GameState.Running,
          grid.setupGrid(players)
        )
      case GameState.NotStarted =>
        println("Game started with default players: 1 and 2")
        val pls = List(Player("1"), Player("2"))
        new GameManager(
          move,
          pls,
          GameState.Running,
          grid.setupGrid(pls)
        )
      case _ =>
        this
    }
  }

  def endGame(): GameManager = {
    state match {
      case GameState.Running =>
        new GameManager(move, players, GameState.GameOver, grid)
      case _ =>
        this
    }
  }

  def pauseGame(): GameManager = {
    state match {
      case GameState.Running =>
        new GameManager(move, players, GameState.Paused, grid)
      case _ =>
        this
    }
  }

  def resumeGame(): GameManager = {
    state match {
      case GameState.Paused =>
        new GameManager(move, players, GameState.Running, grid)
      case _ =>
        this
    }
  }

  def displayGrid: String = {
    state match {
      case GameState.NotStarted => ""
      case _                    => DisplayRenderer.render(grid)
    }
  }

  def getInfo: String = {
    s"\nState: $state" +
      s"\n${currentPlayer.id}'s turn"
  }
}
