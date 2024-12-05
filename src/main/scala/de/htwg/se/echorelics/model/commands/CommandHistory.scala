package model.commands

import scala.collection.mutable.Stack
import scala.util.{Try, Success, Failure}
import service.GameManager

class CommandHistory {
  val history: Stack[Command] = Stack[Command]()
  val redoStack: Stack[Command] = Stack[Command]()

  def add(command: Command): Unit = {
    history.push(command)
  }

  def undo(gameManager: GameManager, steps: Int = 1): GameManager = {
    val undoSteps = Math.min(steps, history.size)

    def undoRecursively(
        stepsUndone: Int,
        currentGameManager: GameManager
    ): GameManager = {
      if (stepsUndone >= undoSteps || history.isEmpty) {
        currentGameManager
      } else {
        val command = history.pop()
        command.undo(currentGameManager) match {
          case Success(updatedGameManager) =>
            undoRecursively(stepsUndone + 1, updatedGameManager)
          case Failure(_) =>
            undoRecursively(stepsUndone, currentGameManager)
        }
      }
    }

    undoRecursively(0, gameManager)
  }

  def redo(gameManager: GameManager, steps: Int = 1): GameManager = {
    val redoSteps = Math.min(steps, redoStack.size)

    def redoRecursively(
        stepsRedone: Int,
        currentGameManager: GameManager
    ): GameManager = {
      if (stepsRedone >= redoSteps || redoStack.isEmpty) {
        currentGameManager
      } else {
        val command = redoStack.pop()
        command.redo(currentGameManager) match {
          case Success(updatedGameManager) =>
            history.push(command)
            redoRecursively(stepsRedone + 1, updatedGameManager)
          case Failure(_) =>
            redoRecursively(stepsRedone, currentGameManager)
        }
      }
    }

    redoRecursively(0, gameManager)
  }
}
