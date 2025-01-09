package model

import scala.util.{Failure, Try}

import service.IGameManager
import utils.Direction
import model.commandImpl._

trait ICommand {
  def execute(gameManager: IGameManager): Try[IGameManager]
  def undo(gameManager: IGameManager): Try[IGameManager] = Failure(
    new RuntimeException("Undo not implemented")
  )
  def redo(gameManager: IGameManager): Try[IGameManager] = Failure(
    new RuntimeException("Redo not implemented")
  )
}

object ICommand {
  def echoCommand(): ICommand = EchoCommand()
  def gridSizeCommand(): ICommand = GridSizeCommand()
  def moveCommand(direction: Direction): ICommand = MoveCommand(direction)
  def pauseCommand(): ICommand = PauseCommand()
  def playerSizeCommand(): ICommand = PlayerSizeCommand()
  def quitCommand(): ICommand = QuitCommand()
  def resumeCommand(): ICommand = ResumeCommand()
  def startCommand(): ICommand = StartCommand()
  def playCardCommand(index: Int): ICommand = PlayCardCommand(index)

  def createCommandHistory(): CommandHistory = new CommandHistory()
}
