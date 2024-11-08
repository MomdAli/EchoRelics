package de.htwg.se.echorelics.model

import de.htwg.se.echorelics.math.Position

case class Player(id: String, position: Position) {
  def move(position: Position): Player = {
    Player(id, position)
  }
}
