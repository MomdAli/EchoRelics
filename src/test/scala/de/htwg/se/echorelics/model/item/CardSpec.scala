package model.item

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import service.GameManager

class CardSpec extends AnyWordSpec with Matchers {

  "A Card" should {

    val gameManager = GameManager.StartingManager

    "have a score based on its rarity" in {
      val commonCard = new Card {
        val rarity = Rarity.Common
        val name = "Common Card"
        val description = "A common card"
        def play(gameManager: GameManager): GameManager = gameManager
      }
      commonCard.toScore should be(50)

      val rareCard = new Card {
        val rarity = Rarity.Rare
        val name = "Rare Card"
        val description = "A rare card"
        def play(gameManager: GameManager): GameManager = gameManager
      }
      rareCard.toScore should be(400)
    }

    "have a name and description" in {
      val card = new Card {
        val rarity = Rarity.Common
        val name = "Test Card"
        val description = "This is a test card"
        def play(gameManager: GameManager): GameManager = gameManager
      }
      card.name should be("Test Card")
      card.description should be("This is a test card")
    }
  }
}
