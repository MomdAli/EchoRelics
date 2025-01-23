package de.htwg.se.echorelics.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.item.inventoryImpl.Inventory
import model.item.{ICard, Rarity}

class InventorySpec extends AnyWordSpec with Matchers {

    "An Inventory" should {

        "add a card if there is space" in {
            val inventory = Inventory(maxCards = 3)
            val card = new ICard {
                override val rarity = Rarity.Common
                override val name = "Test Card"
                override val description = "A test card."
                override def play(gameManager: service.IGameManager) = gameManager
            }
            inventory.addCard(card) should be(true)
            inventory.cardAt(0) should be(Some(card))
        }

        "not add a card if there is no space" in {
            val inventory = Inventory(maxCards = 1)
            val card1 = new ICard {
                override val rarity = Rarity.Common
                override val name = "Test Card 1"
                override val description = "A test card."
                override def play(gameManager: service.IGameManager) = gameManager
            }
            val card2 = new ICard {
                override val rarity = Rarity.Common
                override val name = "Test Card 2"
                override val description = "Another test card."
                override def play(gameManager: service.IGameManager) = gameManager
            }
            inventory.addCard(card1) should be(true)
            inventory.addCard(card2) should be(false)
        }

        "remove a card by index" in {
            val inventory = Inventory(maxCards = 3)
            val card = new ICard {
                override val rarity = Rarity.Common
                override val name = "Test Card"
                override val description = "A test card."
                override def play(gameManager: service.IGameManager) = gameManager
            }
            inventory.addCard(card)
            inventory.removeCard(0) should be(true)
            inventory.cardAt(0) should be(None)
        }

        "not remove a card if the index is out of bounds" in {
            val inventory = Inventory(maxCards = 3)
            inventory.removeCard(0) should be(false)
        }

        "return a card at a specific index" in {
            val inventory = Inventory(maxCards = 3)
            val card = new ICard {
                override val rarity = Rarity.Common
                override val name = "Test Card"
                override val description = "A test card."
                override def play(gameManager: service.IGameManager) = gameManager
            }
            inventory.addCard(card)
            inventory.cardAt(0) should be(Some(card))
        }

        "return None if there is no card at a specific index" in {
            val inventory = Inventory(maxCards = 3)
            inventory.cardAt(0) should be(None)
        }

        "be full when the maximum number of cards is reached" in {
            val inventory = Inventory(maxCards = 1)
            val card = new ICard {
                override val rarity = Rarity.Common
                override val name = "Test Card"
                override val description = "A test card."
                override def play(gameManager: service.IGameManager) = gameManager
            }
            inventory.addCard(card)
            inventory.isFull should be(true)
        }

        "not be full when the maximum number of cards is not reached" in {
            val inventory = Inventory(maxCards = 3)
            val card = new ICard {
                override val rarity = Rarity.Common
                override val name = "Test Card"
                override val description = "A test card."
                override def play(gameManager: service.IGameManager) = gameManager
            }
            inventory.addCard(card)
            inventory.isFull should be(false)
        }
    }
}