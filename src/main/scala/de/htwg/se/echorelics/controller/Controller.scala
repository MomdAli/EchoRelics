package controller

import scala.io.AnsiColor.{BLUE, RESET}

import service.GameManager
import utils.Command
import model.{Grid, Player}
import model.events.{EventManager, EventListener, GameEvent}
import model.Echo

class Controller(var gameManager: GameManager = GameManager.StartingManager)
    extends EventListener {

  EventManager.subscribe(this)

  // Should handle game logic events
  override def handleEvent(event: GameEvent): Unit = {}

  def handleCommand(command: Command): Unit = {
    gameManager = gameManager.handleCommand(command)
    EventManager.notify(gameManager.event)
  }

  def displayGrid: String = gameManager.grid.toString

  def getInfo: String = {
    s"""
       |Round: ${(gameManager.move / gameManager.players.size).toInt + 1}
       |Player ${BLUE}${gameManager.currentPlayer.id}${RESET}'s turn
       |State: ${gameManager.state}
       |""".stripMargin
  }
}
