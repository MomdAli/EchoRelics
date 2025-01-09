package service

import model.IGrid
import model.config.{Config, Configurator}
import model.events.GameEvent
import model.entity.IEntity
import model.IGridSpawner
import model.item.ICard
import service.serviceImpl.{MenuManager, PausedManager, RunningManager}
import utils.{Direction, GameState, GameMemento}

trait IGameManager {
  // variables
  val move: Int
  val players: List[IEntity]
  val grid: IGrid
  val event: GameEvent
  val echoes: List[IEntity] = List()
  protected val gridSpawner: IGridSpawner = IGridSpawner(config)

  // methods to override
  def state: GameState
  def move(direction: Direction): IGameManager = this
  def quit: IGameManager = this
  def setPlayerSize: IGameManager = this
  def setGridSize: IGameManager = this
  def echo: IGameManager = this
  def start: IGameManager = this
  def pause: IGameManager = this
  def resume: IGameManager = this
  def playerCard(index: Int): Option[ICard] = None
  def spawnRelic: IGameManager = this
  def collectRelic(player: IEntity, relic: IEntity): IGameManager = this

  // already implemented methods
  def round: Int = (move / players.size).toInt + 1

  def currentPlayer: IEntity = {
    players.size match {
      case 0 => IEntity.createPlayer("0")
      case _ => players(move % players.size)
    }
  }

  def currentPlayer(index: Int): IEntity = {
    players.size match {
      case 0 => IEntity.createPlayer("0")
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

  def restore(memento: GameMemento): IGameManager = {
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

object IGameManager {
  def apply(): IGameManager = {
    MenuManager(
      0,
      List(IEntity.createPlayer("1"), IEntity.createPlayer("2")),
      IGrid(10),
      event = GameEvent.OnGameEndEvent
    )
  }

  def apply(players: List[IEntity]): IGameManager = {
    MenuManager(
      0,
      players,
      IGrid(10),
      event = GameEvent.OnGameEndEvent
    )
  }

  def createRunningManager(
      move: Int,
      players: List[IEntity],
      grid: IGrid,
      event: GameEvent
  ): RunningManager = {
    RunningManager(
      move,
      players,
      grid,
      event
    )
  }
}
