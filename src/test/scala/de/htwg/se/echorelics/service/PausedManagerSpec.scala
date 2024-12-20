package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, GameState}
import model.entity.Player
import model.events.GameEvent
import model.commands.{ResumeCommand, QuitCommand, MoveCommand}
import utils.Direction

class PausedManagerSpec extends AnyWordSpec with Matchers {

  "A PausedManager" should {

    val grid = new Grid(10)
    val players = List(Player("player1"), Player("player2"))
    val pausedManager =
      PausedManager(0, players, grid, GameEvent.OnGamePauseEvent)

    "have the correct initial state" in {
      pausedManager.move should be(0)
      pausedManager.players should have size 2
      pausedManager.grid.size should be(10)
      pausedManager.event should be(GameEvent.OnGamePauseEvent)
      pausedManager.state should be(GameState.Paused)
    }

    "validate resume and quit commands" in {
      pausedManager.isValid(ResumeCommand()) should be(true)
      pausedManager.isValid(QuitCommand()) should be(true)
    }

    "invalidate other commands" in {
      pausedManager.isValid(MoveCommand(Direction.Up)) should be(false)
    }

    "resume the game" in {
      val newManager = pausedManager.resume
      newManager shouldBe a[RunningManager]
      newManager.event should be(GameEvent.OnGameResumeEvent)
    }

    "quit to menu manager" in {
      val newManager = pausedManager.quit
      newManager shouldBe a[MenuManager]
      newManager.event should be(GameEvent.OnGameEndEvent)
    }
  }
}
