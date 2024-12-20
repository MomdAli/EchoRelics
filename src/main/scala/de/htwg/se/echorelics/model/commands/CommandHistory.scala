package model.commands

import scala.collection.mutable.Stack
import scala.util.{Try, Success, Failure}
import service.GameManager

class CommandHistory {

  private val undoHistory: Stack[GameMemento] = Stack()
  private val redoHistory: Stack[GameMemento] = Stack()

  def saveState(gameManager: GameManager): Unit = {
    val newMemento = gameManager.createMemento
    undoHistory.push(newMemento)
    redoHistory.clear()
  }

  def undo(gameManager: GameManager): Option[GameManager] = {
    if (undoHistory.nonEmpty) {
      val lastMemento = undoHistory.pop()
      redoHistory.push(gameManager.createMemento)
      Some(gameManager.restore(lastMemento))
    } else {
      None
    }
  }

  def redo(gameManager: GameManager): Option[GameManager] = {
    if (redoHistory.nonEmpty) {
      val lastMemento = redoHistory.pop()
      undoHistory.push(gameManager.createMemento)
      Some(gameManager.restore(lastMemento))
    } else {
      None
    }
  }
}
