package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Player}
import utils.Command
import model.config.Config
import model.events.{GameEvent, EventManager, EventListener}

class PlayerSizeManagerSpec extends AnyWordSpec with Matchers {

  "A PlayerSizeManager" should {

    "initialize with default values" in {
      val manager = PlayerSizeManager(
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

    "increase player size when MoveUp command is given" in {
      val manager = PlayerSizeManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.MoveUp)
      updatedManager.players should have size 3
      updatedManager.event should be(GameEvent.OnSetPlayerSizeEvent(3))
    }

    "not increase player size beyond 4 players" in {
      val manager = PlayerSizeManager(
        0,
        List(Player("1"), Player("2"), Player("3"), Player("4")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.MoveUp)
      updatedManager.players should have size 4
      updatedManager.event should be(GameEvent.OnSetPlayerSizeEvent(4))
    }

    "decrease player size when MoveDown command is given" in {
      val manager = PlayerSizeManager(
        0,
        List(Player("1"), Player("2"), Player("3")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.MoveDown)
      updatedManager.players should have size 2
      updatedManager.event should be(GameEvent.OnSetPlayerSizeEvent(2))
    }

    "not decrease player size below 2 players" in {
      val manager = PlayerSizeManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.MoveDown)
      updatedManager.players should have size 2
      updatedManager.event should be(GameEvent.OnSetPlayerSizeEvent(2))
    }

    "transition to MenuManager on Quit command" in {
      val manager = PlayerSizeManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.Quit)
      updatedManager shouldBe a[MenuManager]
      updatedManager.event should be(GameEvent.OnGameEndEvent)
    }

    "handle unknown command" in {
      val manager = PlayerSizeManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.None)
      updatedManager shouldBe a[PlayerSizeManager]
      updatedManager.event should be(GameEvent.OnNoneEvent)
    }
  }
}
