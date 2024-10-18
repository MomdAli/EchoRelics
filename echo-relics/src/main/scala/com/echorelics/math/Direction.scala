package com.echorelics.math

sealed trait Direction
object Direction {
  case object Up extends Direction
  case object Down extends Direction
  case object Left extends Direction
  case object Right extends Direction
}

object DirectionOps {
  def opposite(direction: Direction): Direction = direction match {
    case Direction.Up    => Direction.Down
    case Direction.Down  => Direction.Up
    case Direction.Left  => Direction.Right
    case Direction.Right => Direction.Left
  }
}
