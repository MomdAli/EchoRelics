package com.echorelics.utils

import com.echorelics.math.Direction
import com.echorelics.core.GameManager
import scala.io.StdIn

object InputHandler {
  def getDirectionInput(gameManager: GameManager): Option[Direction] = {
    val input = StdIn.readLine(
      "Enter a direction (W=Up, S=Down, A=Left, D=Right or 'exit' to quit): "
    )

    if (input == null || input.trim.isEmpty) {
      None
    } else {
      val trimmedInput = input.trim.toUpperCase
      trimmedInput match {
        case "EXIT" | "QUIT" | "STOP" | "END" =>
          gameManager.endGame()
          None
        case "W" => Some(Direction.Up)
        case "S" => Some(Direction.Down)
        case "A" => Some(Direction.Left)
        case "D" => Some(Direction.Right)
        case _   => None
      }
    }
  }
}
