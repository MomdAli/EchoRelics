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

    "increase grid size on move up" in {
      val newManager = gridSizeManager.moveUp
      newManager.grid.size should be(11)
      newManager.event should be(GameEvent.OnSetGridSizeEvent(11))
    }

    "decrease grid size on move down" in {
      val newManager = gridSizeManager.moveDown
      newManager.grid.size should be(10) // minimum value constraint
      newManager.event should be(GameEvent.OnSetGridSizeEvent(10))
    }

    "quit to menu manager" in {
      val newManager = gridSizeManager.quit
      newManager shouldBe a[MenuManager]
      newManager.event should be(GameEvent.OnGameEndEvent)
    }
  }
}
