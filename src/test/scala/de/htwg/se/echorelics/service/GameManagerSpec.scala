package service

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import service.serviceImpl._
import utils.Direction
import model.gridImpl.Grid
import model.entity.entityImpl.Player
import model.events.GameEvent

class GameManagerSpec extends AnyWordSpec with Matchers {

  val grid = new Grid(10)
  val players = List(Player("1"), Player("2"))
  val menuManager = MenuManager(0, players, grid, GameEvent.OnNoneEvent)
  val runningManager = RunningManager(0, players, grid, GameEvent.OnNoneEvent)
  val pausedManager = PausedManager(0, players, grid, GameEvent.OnNoneEvent)
  val winnerManager = WinnerManager(0, players, grid, GameEvent.OnNoneEvent)
  val gridSizeManager = GridSizeManager(0, players, grid, GameEvent.OnNoneEvent)
  val playerSizeManager =
    PlayerSizeManager(0, players, grid, GameEvent.OnNoneEvent)

  "MenuManager" should {

    "initialize correctly" in {
      menuManager should not be null
    }

    "set grid size" in {
      val newManager = menuManager.setGridSize
      newManager shouldBe a[GridSizeManager]
    }

    "set player size" in {
      val newManager = menuManager.setPlayerSize
      newManager shouldBe a[PlayerSizeManager]
    }

    "start the game" in {
      val newManager = menuManager.start
      newManager shouldBe a[PlayerSizeManager]
    }

    "quit the game" in {
      val newManager = menuManager.quit
      newManager shouldBe a[MenuManager]
    }
  }

  "RunningManager" should {

    "initialize correctly" in {
      runningManager should not be null
    }

    "move player" in {
      val newManager = runningManager.move(Direction.Up)
      newManager shouldBe a[RunningManager]
    }

    "move all echoes" in {
      val newManager = runningManager.moveAllEchoes
      newManager shouldBe a[RunningManager]
    }

    "damage player" in {
      val player = runningManager.players.head
      val newManager = runningManager.damagePlayer(player)
      newManager shouldBe a[RunningManager]
    }

    "pause the game" in {
      val newManager = runningManager.pause
      newManager shouldBe a[PausedManager]
    }

    "quit the game" in {
      val newManager = runningManager.quit
      newManager shouldBe a[MenuManager]
    }

    "spawn relic" in {
      val newManager = runningManager.spawnRelic
      newManager shouldBe a[RunningManager]
    }
  }

  "PausedManager" should {

    "initialize correctly" in {
      pausedManager should not be null
    }

    "resume the game" in {
      val newManager = pausedManager.resume
      newManager shouldBe a[RunningManager]
    }

    "pause the game" in {
      val newManager = pausedManager.pause
      newManager shouldBe a[RunningManager]
    }

    "quit the game" in {
      val newManager = pausedManager.quit
      newManager shouldBe a[MenuManager]
    }
  }

  "WinnerManager" should {

    "initialize correctly" in {
      winnerManager should not be null
    }

    "leave to menu" in {
      val newManager = winnerManager.quit
      newManager shouldBe a[MenuManager]
    }

    "echo should leave to menu" in {
      val newManager = winnerManager.echo
      newManager shouldBe a[MenuManager]
    }

    "start should leave to menu" in {
      val newManager = winnerManager.start
      newManager shouldBe a[MenuManager]
    }

    "pause should leave to menu" in {
      val newManager = winnerManager.pause
      newManager shouldBe a[MenuManager]
    }
  }

  "GridSizeManager" should {

    "initialize correctly" in {
      gridSizeManager should not be null
    }

    "start the game" in {
      val newManager = gridSizeManager.start
      newManager shouldBe a[RunningManager]
    }

    "increase grid size" in {
      val newManager = gridSizeManager.move(Direction.Up)
      newManager shouldBe a[GridSizeManager]
    }

    "decrease grid size" in {
      val newManager = gridSizeManager.move(Direction.Down)
      newManager shouldBe a[GridSizeManager]
    }

    "quit to player size manager" in {
      val newManager = gridSizeManager.quit
      newManager shouldBe a[PlayerSizeManager]
    }
  }

  "PlayerSizeManager" should {

    "initialize correctly" in {
      playerSizeManager should not be null
    }

    "increase player size" in {
      val newManager = playerSizeManager.move(Direction.Up)
      newManager shouldBe a[PlayerSizeManager]
    }

    "decrease player size" in {
      val newManager = playerSizeManager.move(Direction.Down)
      newManager shouldBe a[PlayerSizeManager]
    }

    "start the game" in {
      val newManager = playerSizeManager.start
      newManager shouldBe a[GridSizeManager]
    }

    "quit to menu manager" in {
      val newManager = playerSizeManager.quit
      newManager shouldBe a[MenuManager]
    }
  }
}
