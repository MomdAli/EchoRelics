package view

import utils.{InputHandler, Observer}
import controller.Controller

class TUI(controller: Controller) extends Observer {

  controller.add(this)

  def update(): Unit = {
    println(controller.displayGrid)
    println(controller.gameManager.getInfo)
  }

  /** Allowed commands:
    *   - h: Print help
    *   - n <size>: Start a new game with a grid of size x size (default = 10)
    *   - p <id>: Add a player with the given id
    *   - p -<id>: Remove a player with the given id
    *   - q: Quit the game
    * @param input
    *   The input to process
    */
  def processInput(input: String): Unit = {
    input.split(" ").toList match {
      case "h" :: Nil              => printHelp()
      case "n" :: size :: Nil      => controller.initialGame(size.toInt)
      case "n" :: Nil              => controller.initialGame()
      case "p" :: id :: Nil        => controller.addPlayer(id)
      case "p" :: "-" :: id :: Nil => controller.removePlayer(id)
      case "q" :: Nil              => println("Goodbye!")
      case _ => {
        InputHandler.parseInput(input) match {
          case Some(direction) =>
            controller.movePlayer(direction)
          case None => println("Invalid input")
        }
      }
    }
  }

  def printHelp(): Unit = {
    println(
      """Commands:
        |  - h: Print help
        |  - n <size>: Start a new game with a grid of size x size (default = 10)
        |  - p <id>: Add a player with the given id
        |  - p -<id>: Remove a player with the given id
        |  - q: Quit the game
        |""".stripMargin
    )
  }
}
