package utils

enum Direction:
  case Up, Down, Left, Right
  def opposite: Direction = this match {
    case Up    => Down
    case Down  => Up
    case Left  => Right
    case Right => Left
  }
