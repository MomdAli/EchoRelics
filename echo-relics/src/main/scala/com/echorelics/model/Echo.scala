package com.echorelics.model

import com.echorelics.math.Position

final case class Echo(playerID: String, position: Position) {
  def replayAction(): Unit = {
    println(s"Echo of player $playerID replays action at $position")
  }
}
