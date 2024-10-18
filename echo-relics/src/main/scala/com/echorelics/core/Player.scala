package com.echorelics.core

import scala.io.AnsiColor.{RED, RESET}
import com.echorelics.math.{Direction, Position}

final case class Player(id: String, var position: Position) {

  def move(direction: Direction, grid: Grid): Unit = {
    grid.move(this, direction) match {
      case Some(newPosition) =>
        position = newPosition
        println(s"Player $id moved to $newPosition")
      case None =>
        println(s"${RED}Invalid move!${RESET}")
    }
  }

  def leaveEcho(grid: Grid): Echo = {
    val echo = Echo(id, position)

    grid.getTile(position).foreach { tile =>
      tile.content = Some(echo)
    }

    echo
  }
}

// Factory method to create a default player
object Player {
  def defaultPlayer(grid: Grid): Player = {

    val player = new Player(id = "Player1", position = Position(0, 0))

    grid.getTile(Position(0, 0)).foreach { tile =>
      tile.content = Some(player) // Store the player instance
    }

    player
  }
}
