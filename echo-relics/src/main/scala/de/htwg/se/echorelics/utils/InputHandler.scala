package utils

import utils.{Direction, Position}
import scala.io.AnsiColor._

object InputHandler {
  def parseInput(input: String): Option[Direction] = {
    input.toLowerCase() match {
      case "w" => Some(Direction.Up)
      case "s" => Some(Direction.Down)
      case "a" => Some(Direction.Left)
      case "d" => Some(Direction.Right)
      case _   => None
    }
  }

  def invalidMove(reason: String): Unit = {
    print(RED)
    println("Invalid input.")
    println("Reason: " + reason)
    print(RESET)
  }
}
