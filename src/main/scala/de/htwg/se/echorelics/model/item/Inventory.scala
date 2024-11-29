package model.item

// Composite desing pattern for the inventory
case class Inventory(maxCards: Int = 3) {
  private val cards = scala.collection.mutable.ListBuffer[Card]()

  def addCard(card: Card): Boolean = {
    if (cards.size < maxCards) {
      cards += card
      true
    } else {
      false
    }
  }

  // should remove the card and move the other cards to the left
  def removeCard(index: Int): Boolean = {
    if (index >= 0 && index < cards.size) {
      cards.remove(index)
      true
    } else {
      false
    }
  }

  def cardAt(index: Int): Option[Card] = {
    cards.lift(index)
  }

  def isFull: Boolean = cards.size == maxCards
}
