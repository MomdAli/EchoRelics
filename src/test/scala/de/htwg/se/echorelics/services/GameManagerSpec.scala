package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Player, GameState}
import utils.Command
import model.config.Config
import model.events.{EventManager, GameEvent, EventListener}
import model.Echo

class GameManagerSpec extends AnyWordSpec with Matchers {

  "A GameManager" should {

    "initialize with default values" in {
      val manager = GameManager.StartingManager
      manager.move should be(0)
      manager.players should have size 2
      manager.grid.size should be(10)
      manager.config should be(Config.default)
      manager.event should be(GameEvent.OnGameEndEvent)
    }

    "return the current player correctly" in {
      val manager = GameManager.StartingManager
      manager.currentPlayer.id should be("1")
    }

    "calculate the round number correctly" in {
      val manager = GameManager.StartingManager
      manager.round should be(1)
    }

    "handle echo move correctly" in {
      val manager = GameManager.StartingManager
      val echo = Echo("echo1", manager.players.head)
      val updatedManager = manager.handleEchoMove(echo)
      updatedManager should be(manager)
    }

    "handle commands correctly" in {
      val manager = GameManager.StartingManager
      val updatedManager = manager.handleCommand(Command.Quit)
      updatedManager.event should be(GameEvent.OnQuitEvent)
    }
  }
}
