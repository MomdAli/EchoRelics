package model.commands

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import service.GameManager
import scala.util.{Success, Failure}
import utils.Direction

class CommandHistorySpec extends AnyWordSpec with Matchers {

  "A CommandHistory" should {

    "add a command to the history" in {
      val commandHistory = new CommandHistory
      val command = model.commands.MoveCommand(Direction.Up)
      commandHistory.add(command)

      commandHistory.history should not be empty
    }

    "undo the last command" in {
      val commandHistory = new CommandHistory
      val initialGameManager = GameManager()
      val command = model.commands.MoveCommand(Direction.Up)
      commandHistory.add(command)
      val updatedGameManager = commandHistory.undo(initialGameManager)
      updatedGameManager should be(initialGameManager)
    }

    "undo multiple commands" in {
      val commandHistory = new CommandHistory
      val initialGameManager = GameManager()
      val command1 = model.commands.MoveCommand(Direction.Up)
      val command2 = model.commands.MoveCommand(Direction.Up)
      commandHistory.add(command1)
      commandHistory.add(command2)
      val updatedGameManager = commandHistory.undo(initialGameManager, 2)
      updatedGameManager should be(initialGameManager)
    }

    "handle undo with no commands" in {
      val commandHistory = new CommandHistory
      val initialGameManager = GameManager()
      val updatedGameManager = commandHistory.undo(initialGameManager)
      updatedGameManager should be(initialGameManager)
    }

    "handle undo with failure" in {
      val commandHistory = new CommandHistory
      val initialGameManager = GameManager()
      val command = model.commands.MoveCommand(Direction.Up)
      commandHistory.add(command)
      val updatedGameManager = commandHistory.undo(initialGameManager)
      updatedGameManager should be(initialGameManager)
    }
  }
}
