package controller

import scala.io.AnsiColor.{BLUE, RESET}

import model.{Echo, Grid, Player}
import model.events.{EventManager, EventListener, GameEvent}
import service.GameManager
import utils.Command

class Controller(var gameManager: GameManager = GameManager.StartingManager)
    extends EventListener {

  EventManager.subscribe(this)

  // TODO: Implement the event handling
  override def handleEvent(event: GameEvent): Unit = {
    event match {
      case GameEvent.OnNoneEvent => ()
      case _                     => ()
    }
  }

  def handleCommand(command: Command): Unit = {
    gameManager = gameManager.handleCommand(command)
    EventManager.notify(gameManager.event)
  }

  def displayGrid: String = gameManager.grid.toString

  def info: String = {
    s"""
       |Round: ${(gameManager.move / gameManager.players.size).toInt + 1}
       |Player ${BLUE}${gameManager.currentPlayer.id}${RESET}'s turn
       |State: ${gameManager.state}
       |""".stripMargin
  }
}
