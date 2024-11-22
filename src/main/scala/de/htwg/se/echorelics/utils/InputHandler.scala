package utils

import org.jline.terminal.{Terminal, TerminalBuilder}
import org.jline.keymap.KeyMap
import org.jline.reader.Binding
import org.jline.reader.impl.LineReaderImpl

enum Command extends Binding {
  case MoveUp, MoveDown, MoveLeft, MoveRight, SpawnEcho, Quit, None,
    StartGame, PauseGame, ResumeGame, SetGridSize
}

class InputHandler(terminal: Terminal) {
  private var currentInput: Command = Command.None

  private val reader = new LineReaderImpl(terminal)
  private val keyMap = new KeyMap[Binding]

  keyMap.bind(Command.MoveUp: Binding, "\u001b[A") // Arrow Up
  keyMap.bind(Command.MoveDown: Binding, "\u001b[B") // Arrow Down
  keyMap.bind(Command.MoveLeft: Binding, "\u001b[D") // Arrow Left
  keyMap.bind(Command.MoveRight: Binding, "\u001b[C") // Arrow Right
  keyMap.bind(Command.MoveUp: Binding, "w") // W key
  keyMap.bind(Command.MoveDown: Binding, "s") // S key
  keyMap.bind(Command.MoveLeft: Binding, "a") // A key
  keyMap.bind(Command.MoveRight: Binding, "d") // D key

  keyMap.bind(Command.SetGridSize: Binding, "g") // G key

  keyMap.bind(Command.SpawnEcho: Binding, "e") // E key
  keyMap.bind(Command.StartGame: Binding, "s") // S key
  keyMap.bind(Command.PauseGame: Binding, "p") // P key
  keyMap.bind(Command.ResumeGame: Binding, "r") // R key
  keyMap.bind(Command.Quit: Binding, "q") // Q key to quit

  def getCurrentInput: Command = {
    val binding: Binding = reader.readBinding(keyMap)
    binding match {
      case command: Command =>
        currentInput = command
      case _ =>
        currentInput = Command.None
    }
    currentInput
  }
}
