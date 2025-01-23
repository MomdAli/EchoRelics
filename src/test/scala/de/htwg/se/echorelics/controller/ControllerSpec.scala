package controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

import model.ICommand
import model.events.{EventManager, GameEvent}
import service.IGameManager
import scala.util.{Success, Failure}
import model.entity.IEntity

class ControllerSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A Controller" should {

    "handle OnPlayerMoveEvent" in {
      val mockGameManager = mock[IGameManager]
      when(mockGameManager.moveAllEchoes).thenReturn(mockGameManager)
      when(mockGameManager.event).thenReturn(GameEvent.OnNoneEvent)

      val controller = new Controller()(mockGameManager)
      controller.handleEvent(GameEvent.OnPlayerMoveEvent)

      verify(mockGameManager).moveAllEchoes
      EventManager.isSubscribed(controller) shouldBe true
    }

    "handle OnRelicCollectEvent" in {
      val mockGameManager = mock[IGameManager]
      val player = mock[IEntity]
      val relic = mock[IEntity]
      when(mockGameManager.collectRelic(player, relic))
        .thenReturn(mockGameManager)
      when(mockGameManager.event).thenReturn(GameEvent.OnNoneEvent)

      val controller = new Controller()(mockGameManager)
      controller.handleEvent(GameEvent.OnRelicCollectEvent(player, relic))

      verify(mockGameManager).collectRelic(player, relic)
      EventManager.isSubscribed(controller) shouldBe true
    }

    "handle OnRelicSpawnEvent" in {
      val mockGameManager = mock[IGameManager]
      when(mockGameManager.spawnRelic).thenReturn(mockGameManager)
      when(mockGameManager.event).thenReturn(GameEvent.OnNoneEvent)

      val controller = new Controller()(mockGameManager)
      controller.handleEvent(GameEvent.OnRelicSpawnEvent)

      verify(mockGameManager).spawnRelic
      EventManager.isSubscribed(controller) shouldBe true
    }

    "handle OnPlayerDamageEvent" in {
      val mockGameManager = mock[IGameManager]
      val player = mock[IEntity]
      when(mockGameManager.damagePlayer(player)).thenReturn(mockGameManager)
      when(mockGameManager.event).thenReturn(GameEvent.OnNoneEvent)

      val controller = new Controller()(mockGameManager)
      controller.handleEvent(GameEvent.OnPlayerDamageEvent(player))

      verify(mockGameManager).damagePlayer(player)
      EventManager.isSubscribed(controller) shouldBe true
    }

    "handle command execution successfully" in {
      val mockGameManager = mock[IGameManager]
      val mockCommand = mock[ICommand]
      when(mockCommand.execute(mockGameManager))
        .thenReturn(Success(mockGameManager))

      val controller = new Controller()(mockGameManager)
      controller.handleCommand(mockCommand) shouldBe a[Success[?]]

      verify(mockCommand).execute(mockGameManager)
      EventManager.isSubscribed(controller) shouldBe true
    }

    "handle command execution failure" in {
      val mockGameManager = mock[IGameManager]
      val mockCommand = mock[ICommand]
      when(mockCommand.execute(mockGameManager))
        .thenReturn(Failure(new RuntimeException("Error")))

      val controller = new Controller()(mockGameManager)
      controller.handleCommand(mockCommand) shouldBe a[Success[?]]

      verify(mockCommand).execute(mockGameManager)
      EventManager.isSubscribed(controller) shouldBe true
    }

    "load saved game" in {
      val mockGameManager = mock[IGameManager]
      val controller = new Controller()(mockGameManager)

      controller.loadSave(mockGameManager)
      controller.gameManager shouldBe mockGameManager
      EventManager.isSubscribed(controller) shouldBe true
    }
  }
}
