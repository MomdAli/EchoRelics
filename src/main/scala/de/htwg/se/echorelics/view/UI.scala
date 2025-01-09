package view

import model.events.EventListener
import service.IGameManager

/** Trait representing the User Interface (UI) for the EchoRelics game. This
  * trait defines the essential methods required for initializing, processing
  * input, rendering, and closing the UI.
  */
trait UI extends EventListener {

  /** Initializes the UI components and sets up necessary configurations.
    */
  def initialize(): Unit

  /** Gets the input from the user, translates it into a command, and executes
    * the command through the controller.
    */
  def processInput(): Unit

  /** Renders the UI components to display the current state.
    */
  def render(gameManager: IGameManager): Unit

  /** Closes the UI and performs any necessary cleanup.
    */
  def close(): Unit
}
