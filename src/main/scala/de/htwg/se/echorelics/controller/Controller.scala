package controller

import model.Player
import services.events.{
  EventManager,
  OnAddPlayerEvent,
  OnGamePauseEvent,
  OnGameResumeEvent,
  OnGameStartEvent,
  OnPlayerMoveEvent,
  OnRemovePlayerEvent,
  OnSetGridSizeEvent,
  OnSpawnEchoEvent
}
import utils.Direction
import services.GameManager
import model.Grid

class Controller(var gameManager: GameManager) extends EventManager {

  def setGridSize(size: Int): Unit = {
    gameManager = gameManager.setGrid(new Grid(size))
    notifyListeners(OnSetGridSizeEvent(size))
  }

  def startGame: Unit = {
    gameManager = gameManager.startGame()
    notifyListeners(OnGameStartEvent())
  }

  def pauseGame: Unit = {
    gameManager = gameManager.pauseGame()
    notifyListeners(OnGamePauseEvent())
  }

  def resumeGame: Unit = {
    gameManager = gameManager.resumeGame()
    notifyListeners(OnGameResumeEvent())
  }

  def addPlayer(player: Player): Unit = {
    gameManager = gameManager.addPlayer(player)
    notifyListeners(OnAddPlayerEvent(player))
  }

  def removePlayer(player: Player): Unit = {
    gameManager = gameManager.removePlayer(player)
    notifyListeners(OnRemovePlayerEvent(player))
  }

  def movePlayer(direction: Direction): Unit = {
    gameManager = gameManager.moveNextPlayer(direction)
    notifyListeners(OnPlayerMoveEvent(direction))
  }

  def spawnEcho: Unit = {
    gameManager = gameManager.spawnEcho()
    notifyListeners(OnSpawnEchoEvent())
  }

  def displayGrid: Unit = gameManager.displayGrid
}
