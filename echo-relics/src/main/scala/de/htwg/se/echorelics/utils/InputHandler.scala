package de.htwg.se.echorelics.utils

import scala.io.StdIn
import de.htwg.se.echorelics.math.Direction
import de.htwg.se.echorelics.core.GameManager

enum Input {
  case Move(direction: Direction)
  case Quit
}

object InputHandler {
  def handleInput(input: String): Input = {
    input.toLowerCase() match {
      case "w" => Input.Move(Direction.Up)
      case "a" => Input.Move(Direction.Left)
      case "s" => Input.Move(Direction.Down)
      case "d" => Input.Move(Direction.Right)
      case "q" | "quit" | "stop" | "exit"  => Input.Quit
      case _ => throw new IllegalArgumentException("Invalid input")
    }
  }
}
