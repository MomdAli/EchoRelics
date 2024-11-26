package utils

import org.jline.terminal.{Terminal, TerminalBuilder}
import org.jline.keymap.{BindingReader, KeyMap}

enum Command {
  case MoveUp, MoveDown, MoveLeft, MoveRight, SpawnEcho,
    StartGame, PauseGame, ResumeGame, SetPlayerSize, SetGridSize, Quit, None
}

class InputHandler(val terminal: Terminal) {

  private val bindingReader = new BindingReader(terminal.reader())
  private val keyMap = new KeyMap[Command]

  // Mapping keys to commands
  keyMap.bind(Command.MoveUp, "w")
  keyMap.bind(Command.MoveDown, "s")
  keyMap.bind(Command.MoveLeft, "a")
  keyMap.bind(Command.MoveRight, "d")
  keyMap.bind(Command.SpawnEcho, "e")
  keyMap.bind(Command.StartGame, "n")
  keyMap.bind(Command.PauseGame, "p")
  keyMap.bind(Command.ResumeGame, "r")
  keyMap.bind(Command.SetPlayerSize, "z")
  keyMap.bind(Command.SetGridSize, "g")
  keyMap.bind(Command.Quit, "q")

  // Arrow keys
  keyMap.bind(Command.MoveUp, "\u001B[A")
  keyMap.bind(Command.MoveDown, "\u001B[B")
  keyMap.bind(Command.MoveLeft, "\u001B[D")
  keyMap.bind(Command.MoveRight, "\u001B[C")

  def getCurrentInput: Command = {
    try {
      Option(bindingReader.readBinding(keyMap)) match {
        case Some(command: Command) => command
        case _                      => Command.None
      }
    } catch {
      case e: Exception =>
        println(s"Error reading input: ${e.getMessage}")
        Command.None
    }
  }
}
