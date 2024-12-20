package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import model.{Grid, GameState}
import model.config.Config
import model.events.GameEvent
import model.entity.{Player, Relic, Echo}
import model.commands.{Command, GameMemento}
import model.generator.GridSpawner
import model.item.Card
import utils.Direction

class GameManagerSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A GameManager" should {

    val grid = new Grid(10)
    val players = List(Player("player1"), Player("player2"))
    val gameManager = GameManager(players)

    "have the correct initial state" in {
      gameManager.move should be(0)
      gameManager.players should have size 2
      gameManager.grid.size should be(10)
      gameManager.event should be(GameEvent.OnGameEndEvent)
      gameManager.state should be(GameState.NotStarted)
    }

    "return the correct round number" in {
      gameManager.round should be(1)
    }

    "return the correct current player" in {
      gameManager.currentPlayer should be(players.head)
    }

    "return the correct current player by index" in {
      gameManager.currentPlayer(1) should be(players(1))
    }

    "create a valid memento" in {
      val memento = gameManager.createMemento
      memento.grid should be(grid)
      memento.state should be(GameState.NotStarted)
    }

    "restore from a memento correctly" in {
      val memento = gameManager.createMemento
      val restoredManager = gameManager.restore(memento)
      restoredManager.state should be(memento.state)
    }

    "validate commands correctly" in {
      val command = mock[Command]
      gameManager.isValid(command) should be(false)
    }

    "move in a direction correctly" in {
      val newManager = gameManager.move(Direction.Up)
      newManager should be(gameManager)
    }

    "quit the game correctly" in {
      val newManager = gameManager.quit
      newManager.event should not be (gameManager.event)
    }

    "set player size correctly" in {
      val newManager = gameManager.setPlayerSize
      newManager should not be (gameManager)
    }

    "set grid size correctly" in {
      val newManager = gameManager.setGridSize
      newManager should not be (gameManager)
    }

    "echo correctly" in {
      val newManager = gameManager.echo
      newManager should be(gameManager)
    }

    "start the game correctly" in {
      val newManager = gameManager.start
      newManager should not be (gameManager)
    }

    "pause the game correctly" in {
      val newManager = gameManager.pause
      newManager should be(gameManager)
    }

    "resume the game correctly" in {
      val newManager = gameManager.resume
      newManager should be(gameManager)
    }

    "return the correct player card" in {
      gameManager.playerCard(0) should be(None)
    }

    "spawn a relic correctly" in {
      val newManager = gameManager.spawnRelic
      newManager should be(gameManager)
    }

    "collect a relic correctly" in {
      val player = players.head
      val relic = mock[Relic]
      val newManager = gameManager.collectRelic(player, relic)
      newManager should be(gameManager)
    }
  }
}
