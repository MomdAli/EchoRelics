package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, GameState}
import model.config.Configurator
import model.entity.Player
import model.events.GameEvent
import model.entity.Relic

class GameManagerSpec extends AnyWordSpec with Matchers {

  "A GameManager" should {

    val gameManager = GameManager.StartingManager

    "have the correct initial state" in {
      gameManager.move should be(0)
      gameManager.players should have size 2
      gameManager.grid.size should be(10)
      gameManager.event should be(GameEvent.OnGameEndEvent)
    }

    "return the correct round number" in {
      gameManager.round should be(1)
    }

    "return the correct current player" in {
      gameManager.currentPlayer.id should be("1")
    }

    "return the correct current player by index" in {
      gameManager.currentPlayer(1).id should be("2")
    }

    "have a valid configuration" in {
      val config = gameManager.config
      config.playerSize should be(2)
      config.gridSize should be(10)
    }

    "not move up by default" in {
      gameManager.moveUp should be(gameManager)
    }

    "not move down by default" in {
      gameManager.moveDown should be(gameManager)
    }

    "not move right by default" in {
      gameManager.moveRight should be(gameManager)
    }

    "not move left by default" in {
      gameManager.moveLeft should be(gameManager)
    }

    "not echo by default" in {
      gameManager.echo should be(gameManager)
    }

    "not pause by default" in {
      gameManager.pause should be(gameManager)
    }

    "not resume by default" in {
      gameManager.resume should be(gameManager)
    }

    "return None for player card by default" in {
      gameManager.playerCard(0) should be(None)
    }

    "not collect relic by default" in {
      val player = Player("1")
      val relic = Relic()
      gameManager.collectRelic(player, relic) should be(gameManager)
    }
  }
}
