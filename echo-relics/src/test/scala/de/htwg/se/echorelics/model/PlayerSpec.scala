package de.htwg.se.echorelics.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.echorelics.math.Position

class PlayerSpec extends AnyWordSpec with Matchers {
  "A Player" should {
    val initialPosition = Position(0, 0)
    val player = Player("player1", initialPosition)

    "have an id" in {
      player.id should be("player1")
    }

    "have a position" in {
      player.position should be(initialPosition)
    }

    "move to a new position" in {
      val newPosition = Position(1, 1)
      val movedPlayer = player.move(newPosition)
      movedPlayer.position should be(newPosition)
      movedPlayer.id should be(player.id)
    }

    "not change id when moved" in {
      val newPosition = Position(2, 2)
      val movedPlayer = player.move(newPosition)
      movedPlayer.id should be(player.id)
    }
  }
}
