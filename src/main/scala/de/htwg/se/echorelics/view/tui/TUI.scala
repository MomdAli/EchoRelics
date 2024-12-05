package view.tui

import org.jline.terminal.{Terminal, TerminalBuilder}
import scala.util.{Try, Success, Failure}

import controller.Controller
import model.GameState
import model.events.{EventManager, GameEvent}
import view.UI
import service.GameManager

class TUI(controller: Controller) extends UI {

  private def writeln(s: String): Unit = {
    terminal.writer().println(s)
    terminal.flush()
  }
  private val terminal: Terminal = {
    TerminalBuilder
      .builder()
      .system(true)
      .build()
  }
  private val inputHandler = new InputHandler(terminal)

  terminal.enterRawMode()
  EventManager.subscribe(this)

  override def handleEvent(event: GameEvent): Unit = {
    event match {
      case GameEvent.OnGameEndEvent =>
        writeln(TextRenderer.renderWelcomeMessage)
      case GameEvent.OnSetGridSizeEvent(size) =>
        writeln(TextRenderer.renderSizePrompt(size, "Grid"))
      case GameEvent.OnSetPlayerSizeEvent(size) =>
        writeln(TextRenderer.clear)
        writeln(TextRenderer.renderSizePrompt(size, "Player"))
      case GameEvent.OnErrorEvent(message) =>
        writeln(TextRenderer.renderError(message))
      case GameEvent.OnInfoEvent(message) =>
        writeln(message)
      case GameEvent.OnPlayCardEvent(card) =>
        render(controller.gameManager)
        writeln(
          TextRenderer.renderCardPlayed(
            controller.gameManager.currentPlayer,
            card
          )
        )
      case GameEvent.OnQuitEvent => close()
      case GameEvent.OnUpdateRenderEvent =>
        render(controller.gameManager)
      case _ =>
    }
  }

  override def initialize(): Unit = {
    writeln(TextRenderer.clear)
    writeln(TextRenderer.renderWelcomeMessage)
    processInput()
  }

  // This method should loop until the game is over
  override def processInput(): Unit = {
    val input = inputHandler.currentInput

    input.foreach { command =>
      controller.handleCommand(command) match {
        case Success(manager) if manager.event != GameEvent.OnQuitEvent =>
          render(manager)
          EventManager.processEvents()
          processInput()
        case Success(_) =>
          close()
        case Failure(exception) =>
          writeln(TextRenderer.renderError(exception.getMessage))
          processInput()
      }
    }
  }

  override def render(gameManager: GameManager): Unit = {

    // Clear the terminal
    writeln(TextRenderer.clear)

    gameManager.state match {
      case GameState.NotStarted =>
        writeln(TextRenderer.renderInputPrompt)
      case GameState.Running => {
        // stats:
        writeln(s"Round: ${gameManager.round}")
        val invs = gameManager.players
        writeln(TextRenderer.renderStats(invs))

        // grid:
        writeln(TextRenderer.render(gameManager.grid))

        // prompt:
        writeln(TextRenderer.renderHelpPrompt)
        writeln(
          TextRenderer.renderCurrentPlayerPrompt(
            gameManager.currentPlayer
          )
        )
        writeln(TextRenderer.renderInputPrompt)
      }

      case GameState.Paused =>
        writeln("Game paused")

      case _ =>
    }
  }

  override def close(): Unit = {
    writeln(TextRenderer.clear)
    writeln("Goodbye!")
    terminal.close()
  }
}
