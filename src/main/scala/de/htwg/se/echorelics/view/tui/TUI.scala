package view.tui

import org.jline.terminal.{Terminal, TerminalBuilder}

import controller.Controller
import model.GameState
import model.events.{EventManager, GameEvent}
import view.UI

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
        writeln(TextRenderer.clear)
        writeln(TextRenderer.renderWelcomeMessage)
      case GameEvent.OnSetGridSizeEvent(size) =>
        writeln(TextRenderer.clear)
        writeln(TextRenderer.renderSizePrompt(size, "Grid"))
      case GameEvent.OnSetPlayerSizeEvent(size) =>
        writeln(TextRenderer.clear)
        writeln(TextRenderer.renderSizePrompt(size, "Player"))
      case GameEvent.OnErrorEvent(message) =>
        writeln(TextRenderer.renderError(message))
      case GameEvent.OnInfoEvent(message) =>
        writeln(message)
      case GameEvent.OnPlayCardEvent(card) =>
        render(controller.gameManager.state)
        writeln(
          TextRenderer.renderCardPlayed(
            controller.gameManager.currentPlayer,
            card
          )
        )
      case GameEvent.OnRelicCollectEvent(player, relic) =>
      case _ =>
        render(controller.gameManager.state)
    }
  }

  override def initialize(): Unit = {
    writeln(TextRenderer.clear)
    writeln(TextRenderer.renderWelcomeMessage)
    processInput()
  }

  // This method should loop until the game is over
  override def processInput(): Unit = {
    val input = inputHandler.getCurrentInput

    input match {
      case Some(command) =>
        val continue = controller.handleCommand(command)
        if (continue) processInput() else close()
      case None =>
        processInput()
    }
  }

  override def render(gameState: GameState): Unit = {

    // Clear the terminal
    writeln(TextRenderer.clear)

    gameState match {
      case GameState.NotStarted =>
        writeln(TextRenderer.renderInputPrompt)
      case GameState.Running => {
        // stats:
        writeln(s"Round: ${controller.gameManager.round}")
        val invs = controller.gameManager.players
        writeln(TextRenderer.renderStats(invs))

        // grid:
        writeln(TextRenderer.render(controller.gameManager.grid))

        // prompt:
        writeln(TextRenderer.renderHelpPrompt)
        writeln(
          TextRenderer.renderCurrentPlayerPrompt(
            controller.gameManager.currentPlayer
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
