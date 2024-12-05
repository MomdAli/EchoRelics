package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, GameState}
import model.entity.Player
import model.events.GameEvent
import model.commands.{MoveCommand, QuitCommand}
import utils.Direction

class GridSizeManagerSpec extends AnyWordSpec with Matchers {

  "A GridSizeManager" should {

    val grid = new Grid(10)
    val players = List(Player("player1"), Player("player2"))
    val gridSizeManager =
      GridSizeManager(0, players, grid, GameEvent.OnGameStartEvent)

    "have the correct initial state" in {
      gridSizeManager.move should be(0)
      gridSizeManager.players should have size 2
      gridSizeManager.grid.size should be(10)
      gridSizeManager.event should be(GameEvent.OnGameStartEvent)
      gridSizeManager.state should be(GameState.NotStarted)
    }

    "validate move and quit commands" in {
      gridSizeManager.isValid(MoveCommand(Direction.Up)) should be(true)
      gridSizeManager.isValid(QuitCommand()) should be(true)
      gridSizeManager.isValid(MoveCommand(Direction.Right)) should be(false)
    }

    "not change grid size on invalid move" in {
      val newManager = gridSizeManager.move(Direction.Left)
      newManager.grid.size should be(10)
      newManager.event should be(GameEvent.OnGameStartEvent)
    }

    "quit to menu manager" in {
      val newManager = gridSizeManager.quit
      newManager shouldBe a[MenuManager]
      newManager.event should be(GameEvent.OnGameEndEvent)
    }

    "handle multiple moves correctly" in {
      val managerAfterUp = gridSizeManager.move(Direction.Up)
      managerAfterUp.grid.size should be(11)
      managerAfterUp.event should be(GameEvent.OnSetGridSizeEvent(11))

      val managerAfterDown = managerAfterUp.move(Direction.Down)
      managerAfterDown.grid.size should be(10)
      managerAfterDown.event should be(GameEvent.OnSetGridSizeEvent(10))
    }

    "not decrease grid size below minimum" in {
      val smallGridManager = gridSizeManager.copy(grid = new Grid(1))
      val newManager = smallGridManager.move(Direction.Down)
      newManager.grid.size should be(1)
      newManager.event should be(GameEvent.OnSetGridSizeEvent(1))
    }
  }
}
