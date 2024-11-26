package service

import model.{Grid, Player, GameState, Echo}
import model.config.Config
import model.events.GameEvent
import utils.Command

trait GameManager {
  // variables
  def move: Int
  def players: List[Player]
  def grid: Grid
  def config: Config
  def event: GameEvent
  def echoes: List[Echo] = List()

  // methods to implement
  def state: GameState
  def handleCommand(command: Command): GameManager

  // already implemented methods
  def currentPlayer: Player = {
    players.size match {
      case 0 => Player("0")
      case _ => players(move % players.size)
    }
  }
  def round: Int = (move / players.size).toInt + 1
  def handleEchoMove(echo: Echo): GameManager = this
}

object GameManager {
  def StartingManager: GameManager = MenuManager(
    0,
    List(Player("1"), Player("2")),
    new Grid(10),
    Config.default,
    GameEvent.OnGameEndEvent
  )
}
