package controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.events.{EventManager, GameEvent}
import service.{GameManager}
import utils.Command
import model.{Grid, Player}

class ControllerSpec extends AnyWordSpec with Matchers {

  "A Controller" should {

    "subscribe to EventManager on creation" in {
      val controller = new Controller()
      EventManager.isSubscribed(controller) should be(true)
    }

    "handle commands and update gameManager" in {
      val controller = new Controller()
      val initialManager = controller.gameManager
      controller.handleCommand(Command.MoveDown)
      controller.gameManager should not be initialManager
    }

    "display the current grid" in {
      val controller = new Controller()
      controller.displayGrid should be(controller.gameManager.grid.toString)
    }

    "provide game information" in {
      val controller = new Controller()
      val info = controller.info
      info should include("Round")
      info should include("Player")
      info should include("State")
    }
  }
}
