package com.echorelics.core

import com.echorelics.utils.InputHandler
import com.echorelics.core.{Echo, Grid, Player}
import com.echorelics.utils.DisplayRenderer

class GameManager(player: Player, grid: Grid) {

  var gameState: GameState = GameState.Running

  def runGame(): Unit = {
    while (gameState == GameState.Running) {
      DisplayRenderer.render(grid)
      handleInput()
      resolveTurn()
    }

    if (gameState == GameState.GameOver) {
      println("Game over!")
    }
  }

  def handleInput(): Unit = {
    InputHandler.getDirectionInput(this) match {
      case Some(direction) => player.move(direction, grid)
      case None =>
        if gameState == GameState.Running then
          println("Invalid input! Please enter W, A, S, D, or 'exit'.")
    }
  }

  def resolveTurn(): Unit = {
    // Random chance of echo spawning
    // if (Math.random() < 0.5) {
    //   val echo = player.leaveEcho(grid)
    // }
    // println(s"Player ${player.id} left an echo at ${echo.position}")
  }

  def endGame(): Unit =
    gameState = GameState.GameOver
}
