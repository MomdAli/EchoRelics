package echorelics

import controller.Controller
import view.tui.TUI
import service.GameManager

@main def echorelics(): Unit = {
  val controller = Controller()
  val tui = TUI(controller)
  tui.initialize()
}
