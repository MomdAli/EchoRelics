package model

import model.{GameState, Player}
import utils.{DisplayRenderer, Direction, InputHandler}

case class GameManager(
    val move: Int = 0,
    val players: List[Player] = List(),
    val state: GameState = GameState.NotStarted,
    val grid: Grid = new Grid(10)
) {

  def currentPlayer: Player = {
    players.size match {
      case 0 => Player("0")
      case _ => players(move % players.size)
    }
  }

  def getState: GameState = state

  def moveNextPlayer(direction: Direction): GameManager = {
    state match {
      case GameState.Running =>
        val newGrid = grid.movePlayer(currentPlayer, direction)
        if (newGrid == grid) {
          InputHandler.invalidMove("Player can't move there")
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
