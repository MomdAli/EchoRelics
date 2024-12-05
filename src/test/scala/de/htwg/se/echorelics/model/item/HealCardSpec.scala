package model.item

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import service.{GameManager, RunningManager}
import model.entity.Player
import model.Stats

class HealCardSpec extends AnyWordSpec with Matchers {

  "A HealCard" should {

    val gameManager = GameManager().start
    val healCard = HealCard()

    "have a rarity of Common" in {
      healCard.rarity should be(Rarity.Common)
    }

    "have a name 'Heal'" in {
      healCard.name should be("Heal")
    }

    "have a description 'Heal the player for 1 health point.'" in {
      healCard.description should be("Heal the player for 1 health point.")
    }

    "heal the current player by 1 health point" in {
      val updatedGameManager = healCard.play(gameManager)

      updatedGameManager.currentPlayer(-1).stats.health should be(4)
    }

    "not change the health of other players" in {
      val updatedGameManager = healCard.play(gameManager)

      val otherPlayers = updatedGameManager.players.filterNot(
        _ == updatedGameManager.currentPlayer(-1)
      )

      otherPlayers.foreach { player =>
        player.stats.health should be(3)
      }
    }
  }
}
