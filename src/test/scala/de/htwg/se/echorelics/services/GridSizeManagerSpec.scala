package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Player}
import utils.Command
import model.config.Config
import model.events.{GameEvent, EventManager, EventListener}

class GridSizeManagerSpec extends AnyWordSpec with Matchers {

  "A GridSizeManager" should {

    "initialize with default values" in {
      val manager = GridSizeManager(
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

    "increase grid size when MoveUp command is given" in {
      val manager = GridSizeManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.MoveUp)
      updatedManager.grid.size should be(11)
      updatedManager.event should be(GameEvent.OnSetGridSizeEvent(11))
    }

    "decrease grid size when MoveDown command is given" in {
      val manager = GridSizeManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(11),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.MoveDown)
      updatedManager.grid.size should be(10)
      updatedManager.event should be(GameEvent.OnSetGridSizeEvent(10))
    }

    "transition to MenuManager on Quit command" in {
      val manager = GridSizeManager(
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
      val manager = GridSizeManager(
        0,
        List(Player("1"), Player("2")),
        new Grid(10),
        Config.default,
        GameEvent.OnNoneEvent
      )
      val updatedManager = manager.handleCommand(Command.None)
      updatedManager shouldBe a[GridSizeManager]
      updatedManager.event should be(GameEvent.OnNoneEvent)
    }
  }
}
