package service

import model.{Grid, GameState}
import model.config.{Config, Configurator}
import model.events.GameEvent
import model.entity.{Echo, Player, Relic}
import model.commands.{Command, GameMemento}
import model.generator.GridSpawner
import model.item.Card
import utils.Direction

trait GameManager {
  // variables
  val move: Int
  val players: List[Player]
  val grid: Grid
  val event: GameEvent
  val echoes: List[Echo] = List()
  protected val gridSpawner: GridSpawner = new GridSpawner(config)

  // methods to override
  def state: GameState
  def isValid(command: Command): Boolean
  def move(direction: Direction): GameManager = this
  def quit: GameManager = this
  def setPlayerSize: GameManager = this
  def setGridSize: GameManager = this
  def echo: GameManager = this
  def start: GameManager = this
  def pause: GameManager = this
  def resume: GameManager = this
  def playerCard(index: Int): Option[Card] = None
  def spawnRelic: GameManager = this
  def collectRelic(player: Player, relic: Relic): GameManager = this

  // already implemented methods
  def round: Int = (move / players.size).toInt + 1

  def currentPlayer: Player = {
    players.size match {
      case 0 => Player("0")
      case _ => players(move % players.size)
    }
  }

  def currentPlayer(index: Int): Player = {
    players.size match {
      case 0 => Player("0")
      case _ => players((move + index) % players.size)
    }
  }

  def config: Config = Configurator
    .apply()
    .withPlayer(players.size)
    .withGrid(grid.size)
    .build

  def createMemento: GameMemento = {
    GameMemento(grid, state)
  }

  def restore(memento: GameMemento): GameManager = {
    state match {
      case GameState.Paused =>
        PausedManager(move, players, memento.grid, event)
      case GameState.Running =>
        RunningManager(move, players, memento.grid, event)
      case GameState.NotStarted =>
        MenuManager(move, players, memento.grid, event)
      case _ =>
        this
    }
  }
}

object GameManager {
  def apply(): GameManager = {
    MenuManager(
      0,
      List(Player("1"), Player("2")),
      new Grid(10),
      event = GameEvent.OnGameEndEvent
    )
  }

  def apply(players: List[Player]): GameManager = {
    MenuManager(
      0,
      players,
      new Grid(10),
      event = GameEvent.OnGameEndEvent
    )
  }
}
