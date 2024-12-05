package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, GameState}
import model.entity.Player
import model.events.GameEvent
import model.commands.{MoveCommand, QuitCommand}
import utils.Direction

class PlayerSizeManagerSpec extends AnyWordSpec with Matchers {

  "A PlayerSizeManager" should {

    val grid = new Grid(10)
    val players = List(Player("player1"), Player("player2"))
    val playerSizeManager =
      PlayerSizeManager(0, players, grid, GameEvent.OnGameStartEvent)

    "have the correct initial state" in {
      playerSizeManager.move should be(0)
      playerSizeManager.players should have size 2
      playerSizeManager.grid.size should be(10)
      playerSizeManager.event should be(GameEvent.OnGameStartEvent)
      playerSizeManager.state should be(GameState.NotStarted)
    }

    "validate move and quit commands" in {
      playerSizeManager.isValid(MoveCommand(Direction.Up)) should be(true)
      playerSizeManager.isValid(MoveCommand(Direction.Down)) should be(true)
      playerSizeManager.isValid(QuitCommand()) should be(true)
      playerSizeManager.isValid(MoveCommand(Direction.Right)) should be(false)
    }

    "quit to menu manager" in {
      val newManager = playerSizeManager.quit
      newManager shouldBe a[MenuManager]
      newManager.event should be(GameEvent.OnGameEndEvent)
    }

    "retain player size when moving left or right" in {
      val newManagerLeft = playerSizeManager.move(Direction.Left)
      newManagerLeft.players.size should be(2)
      newManagerLeft.event should be(GameEvent.OnGameStartEvent)

      val newManagerRight = playerSizeManager.move(Direction.Right)
      newManagerRight.players.size should be(2)
      newManagerRight.event should be(GameEvent.OnGameStartEvent)
    }

    "not change state when moving up or down" in {
      val newManagerUp = playerSizeManager.move(Direction.Up)
      newManagerUp.state should be(GameState.NotStarted)

      val newManagerDown = playerSizeManager.move(Direction.Down)
      newManagerDown.state should be(GameState.NotStarted)
    }

    "not change grid size when increasing or decreasing player size" in {
      val newManagerUp = playerSizeManager.move(Direction.Up)
      newManagerUp.grid.size should be(10)

      val newManagerDown = playerSizeManager.move(Direction.Down)
      newManagerDown.grid.size should be(10)
    }
  }
}
