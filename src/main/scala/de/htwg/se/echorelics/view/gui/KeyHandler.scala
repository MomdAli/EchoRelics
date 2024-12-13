package view.gui

import javafx.scene.input.KeyEvent
import javafx.scene.input.KeyCode

import _root_.model.commands._
import _root_.utils.Direction

class KeyHandler(actionHandler: ActionHandler) {

  def handleKeyInput(event: KeyEvent): Unit = {
    event.getCode match {
      case KeyCode.W =>
        sendCommand(MoveCommand(Direction.Up))
      case KeyCode.S =>
        sendCommand(MoveCommand(Direction.Down))
      case KeyCode.A =>
        sendCommand(MoveCommand(Direction.Left))
      case KeyCode.D =>
        sendCommand(MoveCommand(Direction.Right))
      case KeyCode.UP =>
        sendCommand(MoveCommand(Direction.Up))
      case KeyCode.DOWN =>
        sendCommand(MoveCommand(Direction.Down))
      case KeyCode.LEFT =>
        sendCommand(MoveCommand(Direction.Left))
      case KeyCode.RIGHT =>
        println("right")
        sendCommand(MoveCommand(Direction.Right))
      case KeyCode.DIGIT1 =>
        sendCommand(PlayCardCommand(0))
      case KeyCode.DIGIT2 =>
        sendCommand(PlayCardCommand(1))
      case KeyCode.DIGIT3 =>
        sendCommand(PlayCardCommand(2))
      case KeyCode.E =>
        sendCommand(EchoCommand())
      case KeyCode.N =>
        sendCommand(StartCommand())
      case KeyCode.ESCAPE =>
        sendCommand(PauseCommand())
      case KeyCode.R =>
        sendCommand(ResumeCommand())
      case KeyCode.Z =>
        sendCommand(PlayerSizeCommand())
      case KeyCode.G =>
        sendCommand(GridSizeCommand())
      case KeyCode.Q =>
        sendCommand(QuitCommand())
      case _ =>
    }
  }

  private def sendCommand(command: Command): Unit = {
    actionHandler.sendCommand(command)
  }
}
