package model.commands

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import service.GameManager
import utils.Direction
import scala.util.{Success, Failure}

class CommandSpec extends AnyWordSpec with Matchers {

  "An EchoCommand" should {
    "execute echo on the game manager" in {
      val gameManager = GameManager()
      val command = EchoCommand()
      command.execute(gameManager) shouldBe a[Success[?]]
    }
  }

  "A GridSizeCommand" should {
    "execute setGridSize on the game manager" in {
      val gameManager = GameManager()
      val command = GridSizeCommand()
      command.execute(gameManager) shouldBe a[Success[?]]
    }
  }

  "A MoveCommand" should {
    "execute move on the game manager" in {
      val gameManager = GameManager()
      val command = MoveCommand(Direction.Up)
      command.execute(gameManager) shouldBe a[Success[?]]
    }

    "undo move on the game manager" in {
      val gameManager = GameManager()
      val command = MoveCommand(Direction.Up)
      command.undo(gameManager) shouldBe a[Success[?]]
    }
  }

  "A PauseCommand" should {
    "execute pause on the game manager" in {
      val gameManager = GameManager()
      val command = PauseCommand()
      command.execute(gameManager) shouldBe a[Success[?]]
    }
  }

  "A PlayerSizeCommand" should {
    "execute setPlayerSize on the game manager" in {
      val gameManager = GameManager()
      val command = PlayerSizeCommand()
      command.execute(gameManager) shouldBe a[Success[?]]
    }
  }

  "A QuitCommand" should {
    "execute quit on the game manager" in {
      val gameManager = GameManager()
      val command = QuitCommand()
      command.execute(gameManager) shouldBe a[Success[?]]
    }
  }

  "A ResumeCommand" should {
    "execute resume on the game manager" in {
      val gameManager = GameManager()
      val command = ResumeCommand()
      command.execute(gameManager) shouldBe a[Success[?]]
    }
  }

  "A StartCommand" should {
    "execute start on the game manager" in {
      val gameManager = GameManager()
      val command = StartCommand()
      command.execute(gameManager) shouldBe a[Success[?]]
    }
  }

  "A PlayCardCommand" should {
    "execute play card on the game manager if card exists" in {
      val gameManager = GameManager()
      val command = PlayCardCommand(0)
      command.execute(gameManager) shouldBe a[Failure[?]]
    }

    "fail to execute play card on the game manager if card does not exist" in {
      val gameManager = GameManager()
      val command = PlayCardCommand(999)
      command.execute(gameManager) shouldBe a[Failure[?]]
    }
  }
}
