package model.item.inventoryImpl

import com.google.inject.Inject

import model.item.ICard
import model.item.IInventory

// Composite desing pattern for the inventory
case class Inventory(maxCards: Int = 3) extends IInventory {
  private val cards = scala.collection.mutable.ListBuffer[ICard]()

  override def addCard(card: ICard): Boolean = {
    if (cards.size < maxCards) {
      cards += card
      true
    } else {
      false
    }
  }

  override def setCard(card: ICard): IInventory = {
    if (cards.size < maxCards) {
      cards += card
    }
    this
  }

  // should remove the card and move the other cards to the left
  override def removeCard(index: Int): Boolean = {
    if (index >= 0 && index < cards.size) {
      cards.remove(index)
      true
    } else {
      false
    }
  }

  override def cardAt(index: Int): Option[ICard] = {
    cards.lift(index)
  }

  override def isFull: Boolean = cards.size == maxCards
}
