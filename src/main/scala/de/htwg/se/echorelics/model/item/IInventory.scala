package model.item

import model.item.inventoryImpl.Inventory

trait IInventory {
  def addCard(card: ICard): Boolean
  def removeCard(index: Int): Boolean
  def cardAt(index: Int): Option[ICard]
  def isFull: Boolean
}
