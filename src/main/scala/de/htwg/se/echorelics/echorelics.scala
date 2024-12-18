package echorelics

import controller.Controller
import view.tui.TUI
import service.GameManager
import view.gui.GUI

object EchoRelics {
  @main def main(): Unit = {
    val controller = new Controller()
    new Thread(new Runnable {
      override def run(): Unit = {
        val gui = new GUI(controller)
        gui.main(Array())
      }
    }).start()

    val tui = new TUI(controller)
    tui.initialize()
  }
}
