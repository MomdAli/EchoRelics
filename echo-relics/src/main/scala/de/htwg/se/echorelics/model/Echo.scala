package de.htwg.se.echorelics.model

import de.htwg.se.echorelics.math.Position

final case class Echo(playerID: String, position: Position) {
  def replayAction(): Unit = {
    println(s"Echo of player $playerID replays action at $position")
  }
}
