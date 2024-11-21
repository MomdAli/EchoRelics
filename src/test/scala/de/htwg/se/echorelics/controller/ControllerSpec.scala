package controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{GameManager, Player}
import utils.Direction

class ControllerSpec extends AnyWordSpec with Matchers {

  "A Controller" should {
    val gameManager = GameManager()
    val controller = new Controller(gameManager)

    "display an empty grid when the game has not started" in {
      controller.displayGrid should be("")
    }

    "display the grid with players after the game has started" in {
      controller.addPlayer("1")
      controller.addPlayer("2")
      controller.initialGame()
      val gridDisplay = controller.displayGrid
      gridDisplay should include("1")
      gridDisplay should include("2")
    }

    "update the grid display after a player moves" in {
      controller.addPlayer("1")
      controller.addPlayer("2")
      controller.initialGame()
      val initialGrid = controller.displayGrid
      controller.movePlayer(Direction.Up)
      controller.movePlayer(Direction.Right)
      val updatedGrid = controller.displayGrid
      updatedGrid should not be initialGrid
    }
  }
}
