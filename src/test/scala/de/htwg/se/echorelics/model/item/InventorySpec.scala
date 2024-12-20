package model.item

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import service.GameManager

class InventorySpec extends AnyWordSpec with Matchers {

  "An Inventory" should {
    "add a card if there is space" in {
      val inventory = Inventory()
      val card = new Card {
        val rarity = Rarity.Common
        val name = "Test Card"
        val description = "This is a test card"
        def play(gameManager: GameManager): GameManager = gameManager
      }
      inventory.addCard(card) should be(true)
      inventory.isFull should be(false)
    }

    "not add a card if there is no space" in {
      val inventory = Inventory(1)
      val card1 = new Card {
        val rarity = Rarity.Common
        val name = "Test Card 1"
        val description = "This is a test card 1"
        def play(gameManager: GameManager): GameManager = gameManager
      }
      val card2 = new Card {
        val rarity = Rarity.Common
        val name = "Test Card 2"
        val description = "This is a test card 2"
        def play(gameManager: GameManager): GameManager = gameManager
      }
      inventory.addCard(card1) should be(true)
      inventory.addCard(card2) should be(false)
      inventory.isFull should be(true)
    }

    "remove a card at a valid index" in {
      val inventory = Inventory()
      val card = new Card {
        val rarity = Rarity.Common
        val name = "Test Card"
        val description = "This is a test card"
        def play(gameManager: GameManager): GameManager = gameManager
      }
      inventory.addCard(card)
      inventory.removeCard(0) should be(true)
      inventory.isFull should be(false)
    }

    "not remove a card at an invalid index" in {
      val inventory = Inventory()
      inventory.removeCard(0) should be(false)
    }

    "return a card at a valid index" in {
      val inventory = Inventory()
      val card = new Card {
        val rarity = Rarity.Common
        val name = "Test Card"
        val description = "This is a test card"
        def play(gameManager: GameManager): GameManager = gameManager
      }
      inventory.addCard(card)
      inventory.cardAt(0) should be(Some(card))
    }

    "return None for a card at an invalid index" in {
      val inventory = Inventory()
      inventory.cardAt(0) should be(None)
    }

    "be full when the maximum number of cards is reached" in {
      val inventory = Inventory(1)
      val card = new Card {
        val rarity = Rarity.Common
        val name = "Test Card"
        val description = "This is a test card"
        def play(gameManager: GameManager): GameManager = gameManager
      }
      inventory.addCard(card)
      inventory.isFull should be(true)
    }
  }
}
