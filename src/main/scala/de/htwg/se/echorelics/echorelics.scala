package echorelics

import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions._
import com.google.inject.name.Names

import controller.Controller
import modules.EchorelicsModule
import view.gui.GUI
import view.tui.TUI
import service.IGameManager

object EchoRelics {
  val injector = Guice.createInjector(new EchorelicsModule)
  val controller = new Controller()(
    injector.instance[IGameManager](Names.named("Menu"))
  )

  def main(args: Array[String]): Unit = {
    new Thread(new Runnable {
      override def run(): Unit = {
        val gui = new GUI(controller)
        gui.main(args)
      }
    }).start()

    val tui = new TUI(controller)
    tui.initialize()
  }
}
