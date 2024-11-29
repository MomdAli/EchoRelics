package model.item

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import service.GameManager
import model.entity.Player
import model.Stats
import model.events.{GameEvent, EventManager, EventListener}
import service.RunningManager

class SwapPlayerCardSpec extends AnyWordSpec with Matchers {

  "A SwapPlayerCard" should {

    val swapCard = SwapPlayerCard()
    val player1 = Player("1", Stats(0, 0, 3))
    val player2 = Player("2", Stats(0, 0, 3))
    val gameManager = GameManager.StartingManager.start

    "have the correct rarity, name, and description" in {
      swapCard.rarity should be(Rarity.Rare)
      swapCard.name should be("Swap Player")
      swapCard.description should be("Swap position with another player.")
    }

    "swap positions with another player" in {
      val updatedGameManager = swapCard.play(gameManager)
      val pos1 = updatedGameManager.grid.findPlayer(player1)
      val pos2 = updatedGameManager.grid.findPlayer(player2)

      pos1 should not be empty
      pos2 should not be empty
      pos1 should not be (pos2)
    }

    "trigger OnPlayCardEvent" in {
      val newManager = swapCard.play(gameManager)
      newManager.event should be(GameEvent.OnPlayCardEvent(swapCard))
    }
  }
}
