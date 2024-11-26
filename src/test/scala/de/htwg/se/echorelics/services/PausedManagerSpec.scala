package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Player, GameState}
import utils.Command
import model.config.Config
import model.events.{GameEvent, EventManager, EventListener}

class PausedManagerSpec extends AnyWordSpec with Matchers {

  "A PausedManager" should {

    "initialize with given values" in {
      val manager = PausedManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      manager.move should be(0)
      manager.players should have size 2
      manager.grid.size should be(10)
      manager.config should be(Config.default)
      manager.event should be(GameEvent.OnNoneEvent)
      manager.state should be(GameState.Paused)
    }

    "handle ResumeGame command" in {
      val manager = PausedManager(
        0,
        List(Player("1")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.ResumeGame)
      updatedManager shouldBe a[RunningManager]
      updatedManager.event should be(GameEvent.OnGameResumeEvent)
    }

    "handle Quit command" in {
      val manager = PausedManager(
        0,
        List(Player("1")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.Quit)
      updatedManager shouldBe a[MenuManager]
      updatedManager.event should be(GameEvent.OnGameEndEvent)
    }

    "handle unknown command" in {
      val manager = PausedManager(
        0,
        List(Player("1")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.None)
      updatedManager shouldBe a[PausedManager]
      updatedManager.event should be(
        GameEvent.OnErrorEvent("Game is Paused. Press 'r' to resume.")
      )
    }
  }
}
