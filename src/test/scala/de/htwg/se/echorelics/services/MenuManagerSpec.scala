package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Player}
import utils.Command
import model.config.Config
import model.events.{GameEvent, EventManager, EventListener}

class MenuManagerSpec extends AnyWordSpec with Matchers {

  "A MenuManager" should {

    "initialize with default values" in {
      val manager = MenuManager(
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
    }

    "handle SetGridSize command" in {
      val manager = MenuManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.SetGridSize)
      updatedManager shouldBe a[GridSizeManager]
      updatedManager.event should be(GameEvent.OnSetGridSizeEvent(10))
    }

    "handle SetPlayerSize command" in {
      val manager = MenuManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.SetPlayerSize)
      updatedManager shouldBe a[PlayerSizeManager]
      updatedManager.event should be(GameEvent.OnSetPlayerSizeEvent(2))
    }

    "handle StartGame command" in {
      val manager = MenuManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.StartGame)
      updatedManager shouldBe a[RunningManager]
      updatedManager.event should be(GameEvent.OnGameStartEvent)
    }

    "handle Quit command" in {
      val manager = MenuManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.Quit)
      updatedManager.event should be(GameEvent.OnQuitEvent)
    }

    "handle unknown command" in {
      val manager = MenuManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.None)
      updatedManager.event should be(GameEvent.OnNoneEvent)
    }
  }
}
