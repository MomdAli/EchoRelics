package model.item

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.scalatestplus.mockito.MockitoSugar
import model.item.itemImpl._
import service.IGameManager
import model.events.GameEvent
import model.entity.IEntity
import model.IGrid
import utils.{Position, Stats}
import com.google.inject.Guice
import modules.EchorelicsModule
import net.codingwell.scalaguice.InjectorExtensions._
import com.google.inject.name.Names

class CardSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A HealCard" should {
    "heal the current player by 1 health point" in {
      val gameManager = mock[IGameManager]
      val currentPlayer = mock[IEntity]
      val healedPlayer = mock[IEntity]

      when(gameManager.currentPlayer).thenReturn(currentPlayer)
      when(currentPlayer.heal).thenReturn(healedPlayer)
      when(gameManager.players).thenReturn(List(currentPlayer))
      when(gameManager.change(any(), any(), any(), any()))
        .thenReturn(gameManager)

      val card = HealCard()
      val newGameManager = card.play(gameManager)

      verify(currentPlayer).heal
      verify(gameManager).change(
        any(),
        any(),
        any(),
        eqTo(GameEvent.OnPlayCardEvent(card))
      )
    }
  }

  "A StrikeCard" should {
    "deal 1 damage to all other players" in {
      val injector = Guice.createInjector(new EchorelicsModule)
      val gameManager = injector.instance[IGameManager](Names.named("Menu"))

      val card = StrikeCard()
      val newGameManager = card.play(gameManager)
    }
  }

  "A TimeTravelCard" should {
    "trigger a time travel event" in {
      val gameManager = mock[IGameManager]

      val card = TimeTravelCard()
      val newGameManager = card.play(gameManager)

      verify(gameManager, never()).change(any(), any(), any(), any())
    }
  }

  "A SwapPlayerCard" should {
    "swap the current player's position with another player" in {
      val gameManager = mock[IGameManager]
      val currentPlayer = mock[IEntity]
      val otherPlayer = mock[IEntity]
      val grid = mock[IGrid]

      when(gameManager.currentPlayer).thenReturn(currentPlayer)
      when(gameManager.players).thenReturn(List(currentPlayer, otherPlayer))
      when(gameManager.grid).thenReturn(grid)
      when(grid.findPlayer(currentPlayer)).thenReturn(Some(Position(0, 0)))
      when(grid.findPlayer(otherPlayer)).thenReturn(Some(Position(1, 1)))
      when(grid.swap(any(), any())).thenReturn(grid)
      when(gameManager.change(any(), any(), any(), any()))
        .thenReturn(gameManager)

      val card = SwapPlayerCard()
      val newGameManager = card.play(gameManager)

      verify(grid).swap(Position(0, 0), Position(1, 1))
      verify(gameManager).change(
        any(),
        any(),
        any(),
        eqTo(GameEvent.OnPlayCardEvent(card))
      )
    }
  }
}
