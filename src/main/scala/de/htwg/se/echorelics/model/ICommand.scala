package model

import scala.util.{Failure, Try}

import model.commandImpl.CommandHistory
import service.IGameManager
import utils.Direction

trait ICommand {
  def execute(gameManager: IGameManager): Try[IGameManager]
  def undo(gameManager: IGameManager): Try[IGameManager] = Failure(
    new RuntimeException("Undo not implemented")
  )
  def redo(gameManager: IGameManager): Try[IGameManager] = Failure(
    new RuntimeException("Redo not implemented")
  )
}
