package view

import controller.Controller
import utils.{Direction, Command, InputHandler}
import org.jline.terminal.{Terminal, TerminalBuilder}
import model.events.{EventListener, GameEvent}
import model.GameState
import model.events.EventManager

class TUI(controller: Controller) extends EventListener {

  private var continue = true
  private val terminal: Terminal = TerminalBuilder
    .builder()
    .system(true)
    .build()

  terminal.enterRawMode()

  private val inputHandler = new InputHandler(terminal)
  private def writeln(s: Any): Unit = terminal.writer.println(s)

  EventManager.subscribe(this)

  // Should handle display events
  override def handleEvent(event: GameEvent): Unit = {
    event match {
      case GameEvent.OnPlayerMoveEvent | GameEvent.OnGameStartEvent |
          GameEvent.OnGameResumeEvent |
          GameEvent.OnEchoSpawnEvent => { // Update the grid and display it
        writeln(DisplayRenderer.clear)
        writeln(controller.info)
        writeln(controller.displayGrid)
        writeln(DisplayRenderer.renderHelpPrompt)
      }
      case GameEvent.OnGameEndEvent => { // Display the end game message
        writeln(DisplayRenderer.clear)
        writeln(DisplayRenderer.renderWelcomeMessage)
      }
      case GameEvent.OnGamePauseEvent => { // Display the pause message
        writeln("Game paused.")
      }
      case GameEvent
            .OnSetGridSizeEvent(size) => { // Display the grid size prompt
        writeln(DisplayRenderer.clear)
        writeln(DisplayRenderer.renderSizePrompt(size, "Grid"))
      }
      case GameEvent
            .OnSetPlayerSizeEvent(size) => { // Display the player size prompt
        writeln(DisplayRenderer.clear)
        writeln(DisplayRenderer.renderSizePrompt(size, "Player"))
      }
      case GameEvent.OnErrorEvent(message) => { // Display the error message
        writeln(DisplayRenderer.renderError(message))
      }
      case GameEvent.OnQuitEvent => { // Display the quit message
        writeln("Goodbey!")
        terminal.close()
        continue = false
      }
      case _ => {} // Do nothing
    }

    terminal.writer.flush()
  }

  def init(): Unit = {
    writeln(DisplayRenderer.clear)
    writeln(DisplayRenderer.renderWelcomeMessage)

    while (continue) {
      controller.handleCommand(inputHandler.getCurrentInput)
    }
  }
}
