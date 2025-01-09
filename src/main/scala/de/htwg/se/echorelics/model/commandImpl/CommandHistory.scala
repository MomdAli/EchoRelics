package model.commandImpl

import scala.collection.mutable.Stack
import scala.util.{Try, Success, Failure}

import model.ICommand
import service.IGameManager
import utils.GameMemento

class CommandHistory extends ICommand {

  private val undoHistory: Stack[GameMemento] = Stack()
  private val redoHistory: Stack[GameMemento] = Stack()

  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    val newMemento = gameManager.createMemento
    undoHistory.push(newMemento)
    redoHistory.clear()
    Success(gameManager)
  }

  override def undo(gameManager: IGameManager): Try[IGameManager] = {
    if (undoHistory.nonEmpty) {
      val lastMemento = undoHistory.pop()
      redoHistory.push(gameManager.createMemento)
      Success(gameManager.restore(lastMemento))
    } else {
      Failure(new NoSuchElementException("No commands to undo"))
    }
  }

  override def redo(gameManager: IGameManager): Try[IGameManager] = {
    if (redoHistory.nonEmpty) {
      val lastMemento = redoHistory.pop()
      undoHistory.push(gameManager.createMemento)
      Success(gameManager.restore(lastMemento))
    } else {
      Failure(new NoSuchElementException("No commands to redo"))
    }
  }
}
