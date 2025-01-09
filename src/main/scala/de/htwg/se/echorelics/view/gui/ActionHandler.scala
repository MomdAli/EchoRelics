package view.gui

import javafx.fxml.FXML

import scala.util.{Failure, Success}

import _root_.controller.Controller
import _root_.model.ICommand
import _root_.model.events.{EventManager, GameEvent}
import _root_.service.IGameManager
import _root_.utils.Direction

class ActionHandler(gui: GUI, controller: Controller) {

  @FXML
  private def onStartButton(): Unit = {
    sendCommand(ICommand.startCommand())
  }

  @FXML
  private def onGridSizeButton(): Unit = {
    sendCommand(ICommand.gridSizeCommand())
  }

  @FXML
  private def onPlayerSizeButton(): Unit = {
    sendCommand(ICommand.playerSizeCommand())
  }

  @FXML
  private def onQuitButton(): Unit = {
    sendCommand(ICommand.quitCommand())
  }

  @FXML
  private def onMoveRightButton(): Unit = {
    sendCommand(ICommand.moveCommand(Direction.Right))
  }

  @FXML
  private def onMoveUpButton(): Unit = {
    sendCommand(ICommand.moveCommand(Direction.Up))
  }

  @FXML
  private def onMoveDownButton(): Unit = {
    sendCommand(ICommand.moveCommand(Direction.Down))
  }

  @FXML
  private def onMoveLeftButton(): Unit = {
    sendCommand(ICommand.moveCommand(Direction.Left))
  }

  def sendCommand(command: ICommand): Unit = {
    controller.handleCommand(command) match {
      case Success(manager: IGameManager)
          if manager.event != GameEvent.OnQuitEvent =>
      case Success(_) =>
        gui.close()
        System.exit(0)
      case Failure(exception) =>
        println(exception.getMessage)
    }
  }
}
