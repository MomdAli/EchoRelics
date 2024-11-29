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

    "increase player size on move up" in {
      val newManager = playerSizeManager.moveUp
      newManager.players.size should be(3)
      newManager.event should be(GameEvent.OnSetPlayerSizeEvent(3))
    }

    "not increase player size beyond 4" in {
      val managerWithFourPlayers = playerSizeManager.copy(players =
        List(Player("1"), Player("2"), Player("3"), Player("4"))
      )
      val newManager = managerWithFourPlayers.moveUp
      newManager.players.size should be(4)
      newManager.event should be(GameEvent.OnSetPlayerSizeEvent(4))
    }

    "decrease player size on move down" in {
      val managerWithThreePlayers = playerSizeManager.copy(players =
        List(Player("1"), Player("2"), Player("3"))
      )
      val newManager = managerWithThreePlayers.moveDown
      newManager.players.size should be(2)
      newManager.event should be(GameEvent.OnSetPlayerSizeEvent(2))
    }

    "not decrease player size below 2" in {
      val newManager = playerSizeManager.moveDown
      newManager.players.size should be(2)
      newManager.event should be(GameEvent.OnSetPlayerSizeEvent(2))
    }

    "quit to menu manager" in {
      val newManager = playerSizeManager.quit
      newManager shouldBe a[MenuManager]
      newManager.event should be(GameEvent.OnGameEndEvent)
    }
  }
}
