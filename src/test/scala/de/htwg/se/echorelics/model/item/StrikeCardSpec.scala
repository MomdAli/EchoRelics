package model.item

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import service.{GameManager, RunningManager}
import model.entity.Player
import model.Stats
import model.events.{GameEvent, EventManager, EventListener}

class StrikeCardSpec extends AnyWordSpec with Matchers {

  "A StrikeCard" should {

    val strikeCard = StrikeCard()
    val player1 = Player("player1", Stats(0, 0, 3))
    val player2 = Player("player2", Stats(0, 0, 3))
    val gameManager = GameManager().start

    "have the correct rarity, name, and description" in {
      strikeCard.rarity should be(Rarity.Legendary)
      strikeCard.name should be("Strike")
      strikeCard.description should be("Deal 1 damage to all other players.")
    }

    "deal 1 damage to all other players" in {
      val updatedGameManager = strikeCard.play(gameManager)
      updatedGameManager.players
        .find(_.id == "1")
        .get
        .stats
        .health should be(3)
      updatedGameManager.players
        .find(_.id == "2")
        .get
        .stats
        .health should be(2)
    }

    "trigger OnPlayCardEvent" in {
      val newManager = strikeCard.play(gameManager)
      newManager.event should be(GameEvent.OnPlayCardEvent(strikeCard))
    }
  }
}
