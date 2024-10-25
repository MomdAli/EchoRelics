package de.htwg.se.echorelics.core

import de.htwg.se.echorelics.math.Direction
import de.htwg.se.echorelics.utils.{Input, InputHandler, DisplayRenderer}
import de.htwg.se.echorelics.model.Player

class GameManager(player: Player, grid: Grid) {

  var gameState: GameState = GameState.Running

  def runGame(): Unit = {
    while (gameState == GameState.Running) {
      DisplayRenderer.render(grid)
      handleInput()
    }

    if (gameState == GameState.GameOver) {
      println("Game over!")
    }
  }

  def handleInput(): Input = {
    val input = InputHandler.handleInput(scala.io.StdIn.readLine("Enter your move: "))

    input match {
      case Input.Move(direction) => {
        grid.move(player, direction)
        input
      }
      case Input.Quit => {
        gameState = GameState.GameOver
        input
      }
    }
  }
}
