package controller

import utils.Observable
import model.{GameManager, Player}
import utils.Direction

class Controller(var gameManager: GameManager) extends Observable {

  def addPlayer(id: String): Unit = {
    gameManager = gameManager.addPlayer(Player(id))
    notifyObservers
  }

  def removePlayer(id: String): Unit = {
    gameManager = gameManager.removePlayer(Player(id))
    notifyObservers
  }

  def initialGame(size: Int = 10): Unit = {
    gameManager = gameManager.startGame()
    notifyObservers
  }

  def movePlayer(direction: Direction): Unit = {
    gameManager = gameManager.moveNextPlayer(direction)
    notifyObservers
  }

  def displayGrid: String = gameManager.displayGrid
}
