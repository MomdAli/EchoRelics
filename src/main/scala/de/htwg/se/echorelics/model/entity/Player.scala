package model.entity

import model.events.{EventManager, GameEvent}
import model.Stats
import model.item.Inventory

case class Player(
    id: String,
    stats: Stats = Stats(0, 0, 3),
    inventory: Inventory = Inventory()
) extends Entity {
  def takeDamage: Player = {
    val updatedStats = stats.updateHealth(-1)
    if (updatedStats.health <= 0)
      EventManager.notify(GameEvent.OnPlayerDeathEvent(this))
    copy(stats = updatedStats)
  }

  def heal: Player = {
    copy(stats = stats.updateHealth(1))
  }

  override def isWalkable: Boolean = false
  override def isCollectable: Boolean = false
}
