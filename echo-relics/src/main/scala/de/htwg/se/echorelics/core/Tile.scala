package de.htwg.se.echorelics.core

import de.htwg.se.echorelics.math.Position

final case class Tile(
    position: Position = Position(0, 0),
    var content: Option[Any] = None
) {
  def isOccupied(): Boolean = content.isDefined
}
