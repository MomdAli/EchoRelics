package view

import controller.Controller
import services.events.{GameEvent, EventListener}
import utils.{Direction, Command, InputHandler}
import org.jline.terminal.{Terminal, TerminalBuilder}

class TUI(controller: Controller) extends EventListener {

  private val terminal: Terminal = TerminalBuilder.terminal()
  private val inputHandler = new InputHandler(terminal)

  controller.add(this)

  override def handleEvent(event: GameEvent): Unit = {
    event match {
      case _ => {
        println(controller.displayGrid)
        println(controller.gameManager.getInfo)
      }
    }
  }

  def init(): Unit = {
    println("Welcome to Echo Relics!")
    println("Press 's' to start the game")
    println("Press 'q' to quit the game")

    var continue = true
    while (continue) {
      inputHandler.getCurrentInput match {
        case Command.MoveUp =>
          controller.movePlayer(Direction.Up)
        case Command.MoveDown =>
          controller.movePlayer(Direction.Down)
        case Command.MoveLeft =>
          controller.movePlayer(Direction.Left)
        case Command.MoveRight =>
          controller.movePlayer(Direction.Right)
        case Command.SpawnEcho =>
          controller.spawnEcho
        case Command.StartGame =>
          controller.startGame
        case Command.PauseGame =>
          controller.pauseGame
        case Command.ResumeGame =>
          controller.resumeGame
        case Command.SetGridSize =>
          controller.setGridSize(10)
        case Command.Quit =>
          println("Quitting the game...")
          continue = false
        case Command.None => // Do nothing if no valid key pressed
      }
    }
  }
}
