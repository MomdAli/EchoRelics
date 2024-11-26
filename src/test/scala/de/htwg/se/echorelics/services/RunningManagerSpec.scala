package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Player, GameState, Echo}
import utils.{Command, Direction}
import model.config.Config
import model.events.{EventManager, GameEvent, EventListener}
import model.TileContent
import utils.Position
import model.Tile

class RunningManagerSpec extends AnyWordSpec with Matchers {

  "A RunningManager" should {

    "have a Running state" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10),
        Config.default,
        GameEvent.OnGameStartEvent
      )
      manager.state should be(GameState.Running)
    }

    "handle MoveDown command" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10).set(Position(1, 1), Tile(TileContent.Player("1"))),
        Config.default,
        GameEvent.OnGameStartEvent
      )
      val updatedManager = manager.handleCommand(Command.MoveDown)
      updatedManager.move should be(1)
      updatedManager.event should be(GameEvent.OnPlayerMoveEvent)
    }

    "handle MoveUp command" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10).set(Position(1, 1), Tile(TileContent.Player("1"))),
        Config.default,
        GameEvent.OnGameStartEvent
      )
      val updatedManager = manager.handleCommand(Command.MoveUp)
      updatedManager.move should be(1)
      updatedManager.event should be(GameEvent.OnPlayerMoveEvent)
    }

    "handle MoveLeft command" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10).set(Position(1, 1), Tile(TileContent.Player("1"))),
        Config.default,
        GameEvent.OnGameStartEvent
      )
      val updatedManager = manager.handleCommand(Command.MoveLeft)
      updatedManager.move should be(1)
      updatedManager.event should be(GameEvent.OnPlayerMoveEvent)
    }

    "handle MoveRight command" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10).set(Position(1, 1), Tile(TileContent.Player("1"))),
        Config.default,
        GameEvent.OnGameStartEvent
      )
      val updatedManager = manager.handleCommand(Command.MoveRight)
      updatedManager.move should be(1)
      updatedManager.event should be(GameEvent.OnPlayerMoveEvent)
    }

    "handle SpawnEcho command" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10),
        Config.default,
        GameEvent.OnGameStartEvent
      )
      val updatedManager = manager.handleCommand(Command.SpawnEcho)
      updatedManager.echoes should have size 1
      updatedManager.event should be(GameEvent.OnEchoSpawnEvent)
    }

    "handle PauseGame command" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10),
        Config.default,
        GameEvent.OnGameStartEvent
      )
      val updatedManager = manager.handleCommand(Command.PauseGame)
      updatedManager shouldBe a[PausedManager]
      updatedManager.event should be(GameEvent.OnGamePauseEvent)
    }

    "handle Quit command with confirmation" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10),
        Config.default,
        GameEvent.OnGameStartEvent,
        confirmation = true
      )
      val updatedManager = manager.handleCommand(Command.Quit)
      updatedManager shouldBe a[MenuManager]
      updatedManager.event should be(GameEvent.OnGameEndEvent)
    }

    "handle Quit command without confirmation" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10),
        Config.default,
        GameEvent.OnGameStartEvent
      )
      val updatedManager = manager.handleCommand(Command.Quit)
      updatedManager.event shouldBe a[GameEvent.OnErrorEvent]
      updatedManager.asInstanceOf[RunningManager].confirmation should be(true)
    }

    "handle unknown command" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10),
        Config.default,
        GameEvent.OnGameStartEvent
      )
      val updatedManager = manager.handleCommand(Command.None)
      updatedManager.event should be(GameEvent.OnNoneEvent)
    }

    "handle echo move" in {
      val manager = RunningManager(
        0,
        List(Player("1")),
        new Grid(10),
        Config.default,
        GameEvent.OnGameStartEvent
      )
      val echo = Echo("e", manager.players.head)
      val updatedManager = manager.handleEchoMove(echo)
      updatedManager.event should be(GameEvent.OnStealRelicEvent(echo))
    }
  }
}
