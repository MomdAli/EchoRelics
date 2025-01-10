package echorelics

import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions._

import controller.Controller
import modules.EchorelicsModule
import view.gui.GUI
import view.tui.TUI
import service.IGameManager
import com.google.inject.name.Names

object EchoRelics {

  val injector = Guice.createInjector(new EchorelicsModule)
  val controller = new Controller()(
    injector.instance[IGameManager](Names.named("Menu"))
  )

  @main def main(): Unit = {
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
