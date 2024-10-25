package de.htwg.se.echorelics.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.echorelics.math.Position

class PlayerSpec extends AnyWordSpec with Matchers {
  "A Player" should {
    "have an id and a position" in {
      val player = Player("Player1", Position(0, 0))
      player.id should be("Player1")
      player.position should be(Position(0, 0))
    }

    "leave an Echo at its current position" in {
      val player = Player("Player1", Position(1, 1))
      val echo = player.leaveEcho()
      echo.playerID should be("Player1")
      echo.position should be(Position(1, 1))
    }

    "be created with a default player" in {
      val defaultPlayer = Player.defaultPlayer()
      defaultPlayer.id should be("Player1")
      defaultPlayer.position should be(Position(0, 0))
    }

    "update its position" in {
      val player = Player("Player1", Position(0, 0))
      player.position = Position(2, 3)
      player.position should be(Position(2, 3))
    }
  }
}