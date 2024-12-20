package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, GameState}
import model.entity.Player
import model.events.GameEvent
import model.commands.{
  GridSizeCommand,
  PlayerSizeCommand,
  StartCommand,
  QuitCommand,
  MoveCommand
}
import utils.Direction

class MenuManagerSpec extends AnyWordSpec with Matchers {

  "A MenuManager" should {

    val grid = new Grid(10)
    val players = List(Player("player1"), Player("player2"))
    val menuManager = MenuManager(0, players, grid, GameEvent.OnGameStartEvent)

    "have the correct initial state" in {
      menuManager.move should be(0)
      menuManager.players should have size 2
      menuManager.grid.size should be(10)
      menuManager.event should be(GameEvent.OnGameStartEvent)
      menuManager.state should be(GameState.NotStarted)
    }

    "validate grid size, player size, start, and quit commands" in {
      menuManager.isValid(GridSizeCommand()) should be(true)
      menuManager.isValid(PlayerSizeCommand()) should be(true)
      menuManager.isValid(StartCommand()) should be(true)
      menuManager.isValid(QuitCommand()) should be(true)
    }

    "invalidate other commands" in {
      menuManager.isValid(MoveCommand(Direction.Right)) should be(false)
    }

    "set grid size" in {
      val newManager = menuManager.setGridSize
      newManager shouldBe a[GridSizeManager]
      newManager.event should be(GameEvent.OnSetGridSizeEvent(grid.size))
    }

    "set player size" in {
      val newManager = menuManager.setPlayerSize
      newManager shouldBe a[PlayerSizeManager]
      newManager.event should be(GameEvent.OnSetPlayerSizeEvent(players.size))
    }

    "start the game" in {
      val newManager = menuManager.start
      newManager shouldBe a[RunningManager]
      newManager.event should be(GameEvent.OnGameStartEvent)
    }

    "quit the game" in {
      val newManager = menuManager.quit
      newManager.event should be(GameEvent.OnQuitEvent)
    }
  }
}
