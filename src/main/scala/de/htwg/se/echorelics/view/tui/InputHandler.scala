package view.tui

import org.jline.terminal.{Terminal, TerminalBuilder}
import org.jline.keymap.{BindingReader, KeyMap}

import utils.Direction
import model.ICommand

class InputHandler(val terminal: Terminal) {

  private val bindingReader = new BindingReader(terminal.reader())
  private val keyMap = new KeyMap[ICommand]

  // Mapping keys to commands
  keyMap.bind(ICommand.moveCommand(Direction.Up), "w")
  keyMap.bind(ICommand.moveCommand(Direction.Down), "s")
  keyMap.bind(ICommand.moveCommand(Direction.Left), "a")
  keyMap.bind(ICommand.moveCommand(Direction.Right), "d")
  keyMap.bind(ICommand.playCardCommand(0), "1")
  keyMap.bind(ICommand.playCardCommand(1), "2")
  keyMap.bind(ICommand.playCardCommand(2), "3")
  keyMap.bind(ICommand.echoCommand(), "e")
  keyMap.bind(ICommand.startCommand(), "n")
  keyMap.bind(ICommand.pauseCommand(), "p")
  keyMap.bind(ICommand.resumeCommand(), "r")
  keyMap.bind(ICommand.playerSizeCommand(), "z")
  keyMap.bind(ICommand.gridSizeCommand(), "g")
  keyMap.bind(ICommand.quitCommand(), "q")

  def currentInput: Option[ICommand] = {
    val key = bindingReader.readBinding(keyMap)
    Option(key)
  }
}
