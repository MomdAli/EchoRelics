package view.gui

import javafx.scene.input.KeyEvent
import javafx.scene.input.KeyCode

import _root_.model.ICommand
import _root_.utils.Direction

class KeyHandler(actionHandler: ActionHandler) {

  def handleKeyInput(event: KeyEvent): Unit = {
    event.getCode match {
      case KeyCode.W =>
        sendCommand(ICommand.moveCommand(Direction.Up))
      case KeyCode.S =>
        sendCommand(ICommand.moveCommand(Direction.Down))
      case KeyCode.A =>
        sendCommand(ICommand.moveCommand(Direction.Left))
      case KeyCode.D =>
        sendCommand(ICommand.moveCommand(Direction.Right))
      case KeyCode.UP =>
        sendCommand(ICommand.moveCommand(Direction.Up))
      case KeyCode.DOWN =>
        sendCommand(ICommand.moveCommand(Direction.Down))
      case KeyCode.LEFT =>
        sendCommand(ICommand.moveCommand(Direction.Left))
      case KeyCode.RIGHT =>
        sendCommand(ICommand.moveCommand(Direction.Right))
      case KeyCode.DIGIT1 =>
        sendCommand(ICommand.playCardCommand(0))
      case KeyCode.DIGIT2 =>
        sendCommand(ICommand.playCardCommand(1))
      case KeyCode.DIGIT3 =>
        sendCommand(ICommand.playCardCommand(2))
      case KeyCode.E =>
        sendCommand(ICommand.echoCommand())
      case KeyCode.N =>
        sendCommand(ICommand.startCommand())
      case KeyCode.ESCAPE =>
        sendCommand(ICommand.pauseCommand())
      case KeyCode.R =>
        sendCommand(ICommand.resumeCommand())
      case KeyCode.Z =>
        sendCommand(ICommand.playerSizeCommand())
      case KeyCode.G =>
        sendCommand(ICommand.gridSizeCommand())
      case KeyCode.Q =>
        sendCommand(ICommand.quitCommand())
      case _ =>
    }
  }

  private def sendCommand(command: ICommand): Unit = {
    actionHandler.sendCommand(command)
  }
}
