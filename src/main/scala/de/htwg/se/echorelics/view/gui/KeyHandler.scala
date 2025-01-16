package view.gui

import javafx.scene.input.KeyEvent
import javafx.scene.input.KeyCode

import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import _root_.model.ICommand
import _root_.utils.Direction

class KeyHandler(actionHandler: ActionHandler) {

  private val injector = actionHandler.injector

  def handleKeyInput(event: KeyEvent): Unit = {
    event.getCode match {
      case KeyCode.W =>
        sendCommand(injector.instance[ICommand](Names.named("MoveUp")))
      case KeyCode.S =>
        sendCommand(injector.instance[ICommand](Names.named("MoveDown")))
      case KeyCode.A =>
        sendCommand(injector.instance[ICommand](Names.named("MoveLeft")))
      case KeyCode.D =>
        sendCommand(injector.instance[ICommand](Names.named("MoveRight")))
      case KeyCode.UP =>
        sendCommand(injector.instance[ICommand](Names.named("MoveUp")))
      case KeyCode.DOWN =>
        sendCommand(injector.instance[ICommand](Names.named("MoveDown")))
      case KeyCode.LEFT =>
        sendCommand(injector.instance[ICommand](Names.named("MoveLeft")))
      case KeyCode.RIGHT =>
        sendCommand(injector.instance[ICommand](Names.named("MoveRight")))
      case KeyCode.DIGIT1 =>
        sendCommand(injector.instance[ICommand](Names.named("PlayCard0")))
      case KeyCode.DIGIT2 =>
        sendCommand(injector.instance[ICommand](Names.named("PlayCard1")))
      case KeyCode.DIGIT3 =>
        sendCommand(injector.instance[ICommand](Names.named("PlayCard2")))
      case KeyCode.E =>
        sendCommand(injector.instance[ICommand](Names.named("Echo")))
      case KeyCode.N =>
        sendCommand(injector.instance[ICommand](Names.named("Start")))
      case KeyCode.ESCAPE =>
        sendCommand(injector.instance[ICommand](Names.named("Pause")))
      case KeyCode.R =>
        sendCommand(injector.instance[ICommand](Names.named("Resume")))
      case KeyCode.Z =>
        sendCommand(injector.instance[ICommand](Names.named("PlayerSize")))
      case KeyCode.G =>
        sendCommand(injector.instance[ICommand](Names.named("GridSize")))
      case KeyCode.Q =>
        sendCommand(injector.instance[ICommand](Names.named("Quit")))
      case KeyCode.C =>
        sendCommand(injector.instance[ICommand](Names.named("Load")))
      case _ =>
    }
  }

  private def sendCommand(command: ICommand): Unit = {
    actionHandler.sendCommand(command)
  }
}
