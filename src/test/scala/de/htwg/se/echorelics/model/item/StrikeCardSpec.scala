package model.item

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import service.{GameManager, RunningManager}
import model.events.{EventManager, GameEvent}
import model.entity.Player
import model.{Grid, Stats}

class StrikeCardSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A StrikeCard" should {

    "have the correct rarity" in {
      val card = StrikeCard()
      card.rarity should be(Rarity.Legendary)
    }

    "have the correct name" in {
      val card = StrikeCard()
      card.name should be("Strike")
    }

    "have the correct description" in {
      val card = StrikeCard()
      card.description should be("Deal 1 damage to all other players.")
    }

    "deal 1 damage to all other players" in {
      val card = StrikeCard()
      val player1 = Player("player1", Stats(0, 0, 3))
      val player2 = Player("player2", Stats(0, 0, 3))
      val player3 = Player("player3", Stats(0, 0, 3))
      val gameManager = mock[GameManager]
      when(gameManager.players).thenReturn(List(player1, player2, player3))
      when(gameManager.currentPlayer).thenReturn(player1)
      when(gameManager.move).thenReturn(0)
      when(gameManager.grid).thenReturn(mock[Grid])

      val newGameManager = card.play(gameManager)

      newGameManager shouldBe a[RunningManager]
      newGameManager.players should contain theSameElementsAs List(
        player1,
        player2.takeDamage,
        player3.takeDamage
      )
      newGameManager.move should be(1)
      newGameManager.event should be(GameEvent.OnPlayCardEvent(card))
    }

    "not deal damage if there are no other players" in {
      val card = StrikeCard()
      val player1 = Player("player1", Stats(0, 0, 3))
      val gameManager = mock[GameManager]
      when(gameManager.players).thenReturn(List(player1))
      when(gameManager.currentPlayer).thenReturn(player1)

      val newGameManager = card.play(gameManager)

      newGameManager should be(gameManager)
    }
  }
}
