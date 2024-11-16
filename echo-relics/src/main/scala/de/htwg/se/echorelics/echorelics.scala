package echorelics

import controller.Controller
import view.TUI
import model.GameManager

@main def echorelics(): Unit = {
  val controller = Controller(GameManager())
  val tui = TUI(controller)
  tui.init()
}
