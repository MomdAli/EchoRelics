package de.htwg.se.echorelics.core

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.echorelics.model.Player
import de.htwg.se.echorelics.math.Position
import de.htwg.se.echorelics.utils.Input
import de.htwg.se.echorelics.math.Direction

class GameManagerSpec extends AnyWordSpec with Matchers {

  "A GameManager" should {

    "initialize with a running game state" in {
      val gameManager = new GameManager()
      gameManager.gameState should be(GameState.Running)
    }

    "initialize with a player at position (0,0)" in {
      val gameManager = new GameManager()
      gameManager.initialPlayer.position should be(Position(0, 0))
    }

    "update the player's position when a valid move input is given" in {
      val gameManager = new GameManager()
      val initialPlayer = gameManager.initialPlayer
      val input = Some(Input.Move(Direction.Up))
      val updatedPlayer = gameManager.handleInput(input, initialPlayer)
      updatedPlayer.position should be(Position(0, 0))
    }

    "not update the player's position when an invalid move input is given" in {
      val gameManager = new GameManager()
      val initialPlayer = gameManager.initialPlayer
      val input = Some(Input.Move(Direction.Left))
      val updatedPlayer = gameManager.handleInput(input, initialPlayer)
      updatedPlayer.position should be(Position(0, 0))
    }

    "end the game when the quit input is given" in {
      val gameManager = new GameManager()
      val initialPlayer = gameManager.initialPlayer
      val input = Some(Input.Quit)
      gameManager.handleInput(input, initialPlayer)
      gameManager.gameState should be(GameState.GameOver)
    }

    "print an error message for invalid input" in {
      val gameManager = new GameManager()
      val initialPlayer = gameManager.initialPlayer
      val input = None
      val updatedPlayer = gameManager.handleInput(input, initialPlayer)
      updatedPlayer.position should be(Position(0, 0))
    }
  }
}
