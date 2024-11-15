package utils

case class Position(x: Int, y: Int) {
  def +(that: Position): Position = Position(this.x + that.x, this.y + that.y)

  def isWithinBounds(width: Int, height: Int): Boolean = {
    x >= 0 && x < width && y >= 0 && y < height
  }

  def move(direction: Direction): Position = {
    direction match {
      case Direction.Up    => Position(x, y - 1)
      case Direction.Down  => Position(x, y + 1)
      case Direction.Left  => Position(x - 1, y)
      case Direction.Right => Position(x + 1, y)
    }
  }

  override def toString(): String = s"($x, $y)"
}

object Position {
  val Zero: Position = Position(0, 0)
  val Up: Position = Position(0, -1)
  val Down: Position = Position(0, 1)
  val Left: Position = Position(-1, 0)
  val Right: Position = Position(1, 0)
}
