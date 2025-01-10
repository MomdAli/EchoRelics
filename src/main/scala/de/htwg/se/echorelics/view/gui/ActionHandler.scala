package view.gui

import javafx.fxml.FXML

import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._
import scala.util.{Failure, Success}

import _root_.controller.Controller
import _root_.model.ICommand
import _root_.model.events.{EventManager, GameEvent}
import _root_.service.IGameManager
import _root_.utils.Direction
import _root_.modules.EchorelicsModule

class ActionHandler(gui: GUI, controller: Controller) {

  val injector = Guice.createInjector(new EchorelicsModule)

  @FXML
  private def onStartButton(): Unit = {
    sendCommand(injector.instance[ICommand](Names.named("Start")))
  }

  @FXML
  private def onGridSizeButton(): Unit = {
    sendCommand(injector.instance[ICommand](Names.named("GridSize")))
  }

  @FXML
  private def onPlayerSizeButton(): Unit = {
    sendCommand(injector.instance[ICommand](Names.named("PlayerSize")))
  }

  @FXML
  private def onQuitButton(): Unit = {
    sendCommand(injector.instance[ICommand](Names.named("Quit")))
  }

  @FXML
  private def onMoveRightButton(): Unit = {
    sendCommand(injector.instance[ICommand](Names.named("MoveRight")))
  }

  @FXML
  private def onMoveUpButton(): Unit = {
    sendCommand(injector.instance[ICommand](Names.named("MoveUp")))
  }

  @FXML
  private def onMoveDownButton(): Unit = {
    sendCommand(injector.instance[ICommand](Names.named("MoveDown")))
  }

  @FXML
  private def onMoveLeftButton(): Unit = {
    sendCommand(injector.instance[ICommand](Names.named("MoveLeft")))
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
