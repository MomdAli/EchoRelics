package controller

import scala.util.Success

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import model.commands.{Command, CommandHistory}
import model.events.{EventManager, GameEvent}
import model.entity.{Player, Relic}
import service.GameManager

class ControllerSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A Controller" should {

    "handle OnRelicCollectEvent correctly" in {
      val gameManager = mock[GameManager]
      val controller = new Controller(gameManager)
      val player = mock[Player]
      val relic = mock[Relic]

      when(gameManager.collectRelic(player, relic)).thenReturn(gameManager)

      controller.handleEvent(GameEvent.OnRelicCollectEvent(player, relic))

      verify(gameManager).collectRelic(player, relic)
    }

    "handle OnRelicSpawnEvent correctly" in {
      val gameManager = mock[GameManager]
      val controller = new Controller(gameManager)

      when(gameManager.spawnRelic).thenReturn(gameManager)

      controller.handleEvent(GameEvent.OnRelicSpawnEvent)

      verify(gameManager).spawnRelic
    }

    "handle OnTimeTravelEvent correctly" in {
      val gameManager = mock[GameManager]
      val controller = new Controller(gameManager)
      val commandHistory = mock[CommandHistory]
      val commandHistoryField =
        classOf[Controller].getDeclaredField("commandHistory")
      commandHistoryField.setAccessible(true)
      commandHistoryField.set(controller, commandHistory)

      when(commandHistory.undo(gameManager)).thenReturn(Some(gameManager))

      controller.handleEvent(GameEvent.OnTimeTravelEvent(1))

      verify(commandHistory).undo(gameManager)
    }

    "handle invalid command correctly" in {
      val gameManager = mock[GameManager]
      val controller = new Controller(gameManager)
      val command = mock[Command]

      when(gameManager.isValid(command)).thenReturn(false)

      controller.handleCommand(command) should be(Success(gameManager))
    }

    "handle valid command correctly" in {
      val gameManager = mock[GameManager]
      val controller = new Controller(gameManager)
      val command = mock[Command]

      when(gameManager.isValid(command)).thenReturn(true)
      when(command.execute(gameManager)).thenReturn(Success(gameManager))

      controller.handleCommand(command) should be(Success(gameManager))
    }

    "undo correctly" in {
      val gameManager = mock[GameManager]
      val controller = new Controller(gameManager)
      val commandHistory = mock[CommandHistory]
      val commandHistoryField =
        classOf[Controller].getDeclaredField("commandHistory")
      commandHistoryField.setAccessible(true)
      commandHistoryField.set(controller, commandHistory)

      when(commandHistory.undo(gameManager)).thenReturn(Some(gameManager))

      controller.undo(1) should be(Success(gameManager))
    }

    "fail to undo when no actions are available" in {
      val gameManager = mock[GameManager]
      val controller = new Controller(gameManager)
      val commandHistory = mock[CommandHistory]
      val commandHistoryField =
        classOf[Controller].getDeclaredField("commandHistory")
      commandHistoryField.setAccessible(true)
      commandHistoryField.set(controller, commandHistory)

      when(commandHistory.undo(gameManager)).thenReturn(None)

      controller.undo(1).isFailure should be(true)
    }
  }
}
