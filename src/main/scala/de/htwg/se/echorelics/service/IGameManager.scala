package service

import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import modules.{EchorelicsModule, PlayerProvider}
import model.IGrid
import model.config.{Config, Configurator}
import model.events.GameEvent
import model.entity.IEntity
import model.IGridSpawner
import model.item.ICard
import service.serviceImpl.{MenuManager, PausedManager, RunningManager}
import utils.{Direction, GameState, GameMemento}

trait IGameManager {

  val injector = Guice.createInjector(new EchorelicsModule)
  val playerProvider = injector.instance[PlayerProvider]

  // variables
  val move: Int
  val players: List[IEntity]
  val grid: IGrid
  val event: GameEvent
  val echoes: List[IEntity] = List()
  protected val gridSpawner: IGridSpawner = injector.instance[IGridSpawner]

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
      case 0 => {
        playerProvider.setId("0")
        playerProvider.get()
      }
      case _ => players(move % players.size)
    }
  }

  def currentPlayer(index: Int): IEntity = {
    players.size match {
      case 0 => {
        playerProvider.setId("0")
        playerProvider.get()
      }
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

  def change(
      move: Int = this.move,
      players: List[IEntity] = this.players,
      grid: IGrid = this.grid,
      event: GameEvent = this.event
  ): IGameManager = {
    state match {
      case GameState.Paused =>
        PausedManager(move, players, grid, event)
      case GameState.Running =>
        RunningManager(move, players, grid, event)
      case GameState.NotStarted =>
        MenuManager(move, players, grid, event)
      case _ =>
        this
    }
  }
}
