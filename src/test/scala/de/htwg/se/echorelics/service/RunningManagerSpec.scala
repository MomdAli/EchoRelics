package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, GameState}
import model.entity.{Player, Relic}
import model.events.GameEvent
import model.commands.{
  MoveCommand,
  PauseCommand,
  QuitCommand,
  EchoCommand,
  PlayCardCommand,
  PlayerSizeCommand
}
import utils.Direction
import model.item.Card

class RunningManagerSpec extends AnyWordSpec with Matchers {

  "A RunningManager" should {

    val grid = new Grid(10)
    val players = List(Player("player1"), Player("player2"))
    val runningManager =
      RunningManager(0, players, grid, GameEvent.OnGameStartEvent)

    "have the correct initial state" in {
      runningManager.move should be(0)
      runningManager.players should have size 2
      runningManager.grid.size should be(10)
      runningManager.event should be(GameEvent.OnGameStartEvent)
      runningManager.state should be(GameState.Running)
    }

    "validate move, pause, quit, echo, and play card commands" in {
      runningManager.isValid(MoveCommand(Direction.Up)) should be(true)
      runningManager.isValid(PauseCommand()) should be(true)
      runningManager.isValid(QuitCommand()) should be(true)
      runningManager.isValid(EchoCommand()) should be(true)
      runningManager.isValid(PlayCardCommand(0)) should be(true)
    }

    "invalidate other commands" in {
      runningManager.isValid(PlayerSizeCommand()) should be(false)
    }

    "pause the game" in {
      val newManager = runningManager.pause
      newManager shouldBe a[PausedManager]
      newManager.event should be(GameEvent.OnGamePauseEvent)
    }

    "quit to menu manager" in {
      val newManager = runningManager.quit
      newManager shouldBe a[MenuManager]
      newManager.event should be(GameEvent.OnGameEndEvent)
    }

    "return None for player card by default" in {
      runningManager.playerCard(0) should be(None)
    }

    "collect a relic and update player score" in {
      val player = players.head
      val relic = Relic()
      val newManager = runningManager.collectRelic(player, relic)
      newManager.players.head.stats.score should be > 0
      newManager.event shouldBe a[GameEvent.OnInfoEvent]
    }
  }
}
