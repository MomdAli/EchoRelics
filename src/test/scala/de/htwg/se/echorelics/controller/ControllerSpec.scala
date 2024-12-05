package controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import scala.util.{Success, Failure}

import service.GameManager
import model.events.{EventManager, GameEvent}
import model.commands.{Command, CommandHistory}
import model.entity.{Player, Relic}

class ControllerSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A Controller" should {

    "handle OnRelicCollectEvent correctly" in {
      val gameManagerMock = mock[GameManager]
      val controller = new Controller(gameManagerMock)
      val player = Player("1")
      val relic = Relic()

      when(gameManagerMock.collectRelic(player, relic))
        .thenReturn(gameManagerMock)

      controller.handleEvent(GameEvent.OnRelicCollectEvent(player, relic))

      verify(gameManagerMock).collectRelic(player, relic)
    }

    "handle OnRelicSpawnEvent correctly" in {
      val gameManagerMock = mock[GameManager]
      val controller = new Controller(gameManagerMock)

      when(gameManagerMock.spawnRelic).thenReturn(gameManagerMock)

      controller.handleEvent(GameEvent.OnRelicSpawnEvent)

      verify(gameManagerMock).spawnRelic
    }

    "handle OnTimeTravelEvent correctly" in {
      val gameManagerMock = mock[GameManager]
      val commandHistoryMock = mock[CommandHistory]
      val controller = new Controller(gameManagerMock) {
        override val commandHistory: CommandHistory = commandHistoryMock
      }
      val turns = 3

      when(commandHistoryMock.undo(gameManagerMock, turns))
        .thenReturn(gameManagerMock)

      controller.handleEvent(GameEvent.OnTimeTravelEvent(turns))

      verify(commandHistoryMock).undo(gameManagerMock, turns)
    }

    "handle valid command correctly" in {
      val gameManagerMock = mock[GameManager]
      val commandMock = mock[Command]
      val controller = new Controller(gameManagerMock)

      when(gameManagerMock.isValid(commandMock)).thenReturn(true)
      when(commandMock.execute(gameManagerMock))
        .thenReturn(Success(gameManagerMock))

      controller.handleCommand(commandMock) shouldBe Success(gameManagerMock)

      verify(commandMock).execute(gameManagerMock)
    }

    "handle invalid command correctly" in {
      val gameManagerMock = mock[GameManager]
      val commandMock = mock[Command]
      val controller = new Controller(gameManagerMock)

      when(gameManagerMock.isValid(commandMock)).thenReturn(false)

      controller.handleCommand(commandMock) shouldBe Success(gameManagerMock)

      verify(commandMock, never).execute(gameManagerMock)
    }

    "handle command execution failure correctly" in {
      val gameManagerMock = mock[GameManager]
      val commandMock = mock[Command]
      val controller = new Controller(gameManagerMock)

      when(gameManagerMock.isValid(commandMock)).thenReturn(true)
      when(commandMock.execute(gameManagerMock))
        .thenReturn(Failure(new Exception("Execution failed")))

      controller.handleCommand(commandMock) shouldBe Success(gameManagerMock)

      verify(commandMock).execute(gameManagerMock)
    }
  }
}
