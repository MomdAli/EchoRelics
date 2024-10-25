package de.htwg.se.echorelics.model

import scala.io.AnsiColor.{RED, RESET}
import de.htwg.se.echorelics.math.{Direction, Position}
import de.htwg.se.echorelics.core.Grid
import de.htwg.se.echorelics.model.Echo
import de.htwg.se.echorelics.math.Direction

final case class Player(id: String, var position: Position) {
  def leaveEcho(): Echo = {
    Echo(id, position)
  }
}

// Factory method to create a default player
object Player {
  def defaultPlayer(): Player = {
    new Player(id = "Player1", position = Position(0, 0))
  }
}
