package view.tui

import org.jline.terminal.{Terminal, TerminalBuilder}
import org.jline.keymap.{BindingReader, KeyMap}

import utils.Direction
import model.commands.{
  Command,
  GridSizeCommand,
  EchoCommand,
  ResumeCommand,
  StartCommand,
  PauseCommand,
  MoveCommand,
  QuitCommand,
  PlayerSizeCommand,
  PlayCardCommand
}

class InputHandler(val terminal: Terminal) {

  private val bindingReader = new BindingReader(terminal.reader())
  private val keyMap = new KeyMap[Command]

  // Mapping keys to commands
  keyMap.bind(MoveCommand(Direction.Up), "w")
  keyMap.bind(MoveCommand(Direction.Down), "s")
  keyMap.bind(MoveCommand(Direction.Left), "a")
  keyMap.bind(MoveCommand(Direction.Right), "d")
  keyMap.bind(PlayCardCommand(0), "1")
  keyMap.bind(PlayCardCommand(1), "2")
  keyMap.bind(PlayCardCommand(2), "3")
  keyMap.bind(EchoCommand(), "e")
  keyMap.bind(StartCommand(), "n")
  keyMap.bind(PauseCommand(), "p")
  keyMap.bind(ResumeCommand(), "r")
  keyMap.bind(PlayerSizeCommand(), "z")
  keyMap.bind(GridSizeCommand(), "g")
  keyMap.bind(QuitCommand(), "q")

  def getCurrentInput: Option[Command] = {
    val key = bindingReader.readBinding(keyMap)
    Option(key)
  }
}
