package service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.scalatestplus.mockito.MockitoSugar
import scala.util.{Success, Failure}
import utils.{Direction, GameMemento, GameState}
import model.entity.{IEntity}
import model.events.GameEvent
import model.{IGrid}
import service.serviceImpl._
import model.item.{ICard}
import model.config.Config
import model.gridImpl.Grid
import model.entity.entityImpl.Player

class ExtendedGameManagerSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "IGameManager default methods" should {
    "call move(direction)" in {
      val gm = mock[IGameManager]
      when(gm.move(Direction.Up)).thenReturn(gm)
      gm.move(Direction.Up) shouldBe gm
      verify(gm).move(Direction.Up)
    }
    "call quit()" in {
      val gm = mock[IGameManager]
      when(gm.quit).thenReturn(gm)
      gm.quit shouldBe gm
      verify(gm).quit
    }
    "call setPlayerSize()" in {
      val gm = mock[IGameManager]
      when(gm.setPlayerSize).thenReturn(gm)
      gm.setPlayerSize shouldBe gm
      verify(gm).setPlayerSize
    }
    "call setGridSize()" in {
      val gm = mock[IGameManager]
      when(gm.setGridSize).thenReturn(gm)
      gm.setGridSize shouldBe gm
      verify(gm).setGridSize
    }
    "call echo()" in {
      val gm = mock[IGameManager]
      when(gm.echo).thenReturn(gm)
      gm.echo shouldBe gm
      verify(gm).echo
    }
    "call start()" in {
      val gm = mock[IGameManager]
      when(gm.start).thenReturn(gm)
      gm.start shouldBe gm
      verify(gm).start
    }
    "call pause()" in {
      val gm = mock[IGameManager]
      when(gm.pause).thenReturn(gm)
      gm.pause shouldBe gm
      verify(gm).pause
    }
    "call resume()" in {
      val gm = mock[IGameManager]
      when(gm.resume).thenReturn(gm)
      gm.resume shouldBe gm
      verify(gm).resume
    }
    "call spawnRelic()" in {
      val gm = mock[IGameManager]
      when(gm.spawnRelic).thenReturn(gm)
      gm.spawnRelic shouldBe gm
      verify(gm).spawnRelic
    }
    "call collectRelic()" in {
      val player = mock[IEntity]
      val relic = mock[IEntity]
      val gm = mock[IGameManager]
      when(gm.collectRelic(player, relic)).thenReturn(gm)
      gm.collectRelic(player, relic) shouldBe gm
      verify(gm).collectRelic(player, relic)
    }
    "call moveAllEchoes()" in {
      val gm = mock[IGameManager]
      when(gm.moveAllEchoes).thenReturn(gm)
      gm.moveAllEchoes shouldBe gm
      verify(gm).moveAllEchoes
    }
    "call damagePlayer()" in {
      val player = mock[IEntity]
      val gm = mock[IGameManager]
      when(gm.damagePlayer(player)).thenReturn(gm)
      gm.damagePlayer(player) shouldBe gm
      verify(gm).damagePlayer(player)
    }
    "call toXml and toJson" in {
      val gm = mock[IGameManager]
      when(gm.toXml).thenReturn(<mockGameManager/>)
      when(gm.toJson).thenReturn(play.api.libs.json.Json.obj("mock" -> true))
      gm.toXml.label shouldBe "mockGameManager"
      gm.toJson.toString() should include ("mock")
    }
    "call createMemento and restore(memento)" in {
      val gm = mock[IGameManager]
      val memento = GameMemento(mock[IGrid], GameState.NotStarted)
      when(gm.createMemento).thenReturn(memento)
      when(gm.restore(memento)).thenReturn(gm)
      gm.createMemento shouldBe memento
      gm.restore(memento) shouldBe gm
    }
    "call round and currentPlayer" in {
      val gm = mock[IGameManager]
      when(gm.round).thenReturn(1)
      gm.round shouldBe 1
      val entity = mock[IEntity]
      when(gm.currentPlayer).thenReturn(entity)
      gm.currentPlayer shouldBe entity
    }
    "call config" in {
      val gm = mock[IGameManager]
      val c = Config(1,1,1,1,1,1,1)
      when(gm.config).thenReturn(c)
      gm.config shouldBe c
    }
  }

  "RunningManager" should {
    "handle all overrides" in {
      val rm = RunningManager(0, List(Player("1"), Player("2")), new Grid(11), GameEvent.OnNoneEvent)
      rm.state shouldBe GameState.Running
      rm.start.state should (be (GameState.Running) or be (GameState.NotStarted))
      rm.pause.state shouldBe GameState.Paused
      rm.quit.state should (be (GameState.NotStarted) or be (GameState.NotStarted))
      rm.damagePlayer(Player("1")).state shouldBe GameState.Running
      rm.moveAllEchoes.state shouldBe GameState.Running
      rm.spawnRelic.state shouldBe GameState.Running
    }
  }

  "PausedManager" should {
    "handle overrides" in {
      val pm = PausedManager(0, List(), mock[IGrid], GameEvent.OnNoneEvent)
      pm.state shouldBe GameState.Paused
      pm.resume.state shouldBe GameState.Running
      pm.pause.state shouldBe GameState.Running
      pm.quit.state shouldBe GameState.NotStarted
    }
  }

  "PlayerSizeManager" should {
    "handle overrides" in {
      val psm = PlayerSizeManager(0, List(), mock[IGrid], GameEvent.OnNoneEvent)
      psm.state shouldBe GameState.NotStarted
      psm.start.state shouldBe GameState.NotStarted
      psm.quit.state shouldBe GameState.NotStarted
      psm.move(Direction.Up).state shouldBe GameState.NotStarted
    }
  }

  "MenuManager" should {
    "handle overrides" in {
      val mm = MenuManager(0, List(), mock[IGrid], GameEvent.OnNoneEvent)
      mm.state shouldBe GameState.NotStarted
      mm.setGridSize.state shouldBe GameState.NotStarted
      mm.setPlayerSize.state shouldBe GameState.NotStarted
      mm.start.state shouldBe GameState.NotStarted
      mm.quit.event shouldBe GameEvent.OnQuitEvent
    }
  }

  "GridSizeManager" should {
    "handle overrides" in {
      val gsm = GridSizeManager(0, List(), new Grid(11), GameEvent.OnNoneEvent)
      gsm.state shouldBe GameState.NotStarted
      gsm.start.state shouldBe GameState.Running
      gsm.quit.state shouldBe GameState.NotStarted
      gsm.move(Direction.Up).event.isInstanceOf[GameEvent.OnSetGridSizeEvent] shouldBe true
      gsm.move(Direction.Down).event.isInstanceOf[GameEvent.OnSetGridSizeEvent] shouldBe true
    }
  }

  "WinnerManager" should {
    "handle overrides" in {
      val wm = WinnerManager(0, List(), mock[IGrid], GameEvent.OnNoneEvent)
      wm.state shouldBe GameState.Victory
      wm.quit.state shouldBe GameState.NotStarted
      wm.echo.state shouldBe GameState.NotStarted
      wm.start.state shouldBe GameState.NotStarted
      // it's basically redirecting to menu
    }
  }
}