package view.gui

import javafx.fxml.FXML

import scala.util.{Failure, Success}

import _root_.controller.Controller
import _root_.utils.Direction
import _root_.model.commands.Command
import _root_.model.events.{EventManager, GameEvent}
import _root_.model.commands._

class ActionHandler(gui: GUI, controller: Controller) {

  @FXML
  private def onStartButton(): Unit = {
    sendCommand(StartCommand())
  }

  @FXML
  private def onGridSizeButton(): Unit = {
    sendCommand(GridSizeCommand())
  }

  @FXML
  private def onPlayerSizeButton(): Unit = {
    sendCommand(PlayerSizeCommand())
  }

  @FXML
  private def onQuitButton(): Unit = {
    sendCommand(QuitCommand())
  }

  @FXML
  private def onMoveRightButton(): Unit = {
    sendCommand(MoveCommand(Direction.Right))
  }

  @FXML
  private def onMoveUpButton(): Unit = {
    sendCommand(MoveCommand(Direction.Up))
  }

  @FXML
  private def onMoveDownButton(): Unit = {
    sendCommand(MoveCommand(Direction.Down))
  }

  @FXML
  private def onMoveLeftButton(): Unit = {
    sendCommand(MoveCommand(Direction.Left))
  }

  def sendCommand(command: Command): Unit = {
    controller.handleCommand(command) match {
      case Success(manager) if manager.event != GameEvent.OnQuitEvent =>
      case Success(_) =>
        gui.close()
        System.exit(0)
      case Failure(exception) =>
        println(exception.getMessage)
    }
  }
}
