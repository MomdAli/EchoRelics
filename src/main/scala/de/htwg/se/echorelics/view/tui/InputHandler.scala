package view.tui

import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import org.jline.terminal.{Terminal, TerminalBuilder}
import org.jline.keymap.{BindingReader, KeyMap}

import model.ICommand
import modules.EchorelicsModule
import utils.Direction

class InputHandler(val terminal: Terminal) {

  val injector = Guice.createInjector(new EchorelicsModule)

  private val bindingReader = new BindingReader(terminal.reader())
  private val keyMap = new KeyMap[ICommand]

  // Mapping keys to commands
  keyMap.bind(
    injector.instance[ICommand](Names.named("MoveUp")),
    "w"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("MoveDown")),
    "s"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("MoveLeft")),
    "a"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("MoveRight")),
    "d"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("PlayCard0")),
    "1"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("PlayCard1")),
    "2"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("PlayCard2")),
    "3"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("Echo")),
    "e"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("Start")),
    "n"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("Pause")),
    "p"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("Resume")),
    "r"
  )
  // keyMap.bind(
  //   injector.instance[ICommand](Names.named("PlayerSize")),
  //   "z"
  // )
  // keyMap.bind(
  //   injector.instance[ICommand](Names.named("GridSize")),
  //   "g"
  // )
  keyMap.bind(
    injector.instance[ICommand](Names.named("Quit")),
    "q"
  )
  keyMap.bind(
    injector.instance[ICommand](Names.named("Load")),
    "c"
  )

  def currentInput: Option[ICommand] = {
    val key = bindingReader.readBinding(keyMap)
    Option(key)
  }
}
