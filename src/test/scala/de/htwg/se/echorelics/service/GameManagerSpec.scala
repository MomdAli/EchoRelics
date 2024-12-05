package service

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import model.{Grid, GameState}
import model.entity.{Player, Relic}
import model.item.Card
import utils.Direction

class GameManagerSpec extends AnyWordSpec with Matchers {

  "A GameManager" should {

    "initialize with default values" in {
      val gameManager = GameManager()
      gameManager.move should be(0)
      gameManager.players should have size 2
      gameManager.grid.size should be(10)
      gameManager.echoes should be(empty)
    }

    "return the correct round number" in {
      val gameManager = GameManager()
      gameManager.round should be(1)
    }

    "return the current player" in {
      val gameManager = GameManager()
      gameManager.currentPlayer.id should be("1")
    }

    "return the correct player by index" in {
      val gameManager = GameManager()
      gameManager.currentPlayer(1).id should be("2")
    }

    "return a valid configuration" in {
      val gameManager = GameManager()
      val config = gameManager.config
      config.playerSize should be(2)
      config.gridSize should be(10)
    }

    "move in a direction" in {
      val gameManager = GameManager().start
      val newGameManager =
        gameManager.move(Direction.Down).move(Direction.Right)
      newGameManager should not be theSameInstanceAs(gameManager)
    }

    "quit the game" in {
      val gameManager = GameManager()
      val newGameManager = gameManager.quit
      newGameManager should not be theSameInstanceAs(gameManager)
    }

    "set player size" in {
      val gameManager = GameManager()
      val newGameManager = gameManager.setPlayerSize
      newGameManager should not be theSameInstanceAs(gameManager)
    }

    "set grid size" in {
      val gameManager = GameManager()
      val newGameManager = gameManager.setGridSize
      newGameManager should not be theSameInstanceAs(gameManager)
    }

    "echo" in {
      val gameManager = GameManager()
      val newGameManager = gameManager.echo
      newGameManager should be theSameInstanceAs (gameManager)
    }

    "start the game" in {
      val gameManager = GameManager()
      val newGameManager = gameManager.start
      newGameManager should not be theSameInstanceAs(gameManager)
    }

    "pause the game" in {
      val gameManager = GameManager()
      val newGameManager = gameManager.pause
      newGameManager should be theSameInstanceAs (gameManager)
    }

    "resume the game" in {
      val gameManager = GameManager()
      val newGameManager = gameManager.resume
      newGameManager should be theSameInstanceAs (gameManager)
    }

    "return a player card by index" in {
      val gameManager = GameManager()
      gameManager.playerCard(0) should be(None)
    }

    "spawn a relic" in {
      val gameManager = GameManager().start
      val newGameManager = gameManager.spawnRelic
      newGameManager should not be theSameInstanceAs(gameManager)
    }

    "collect a relic" in {
      val gameManager = GameManager()
      val player = Player("1")
      val relic = Relic()
      val newGameManager = gameManager.collectRelic(player, relic)
      newGameManager should be theSameInstanceAs (gameManager)
    }
  }
}
