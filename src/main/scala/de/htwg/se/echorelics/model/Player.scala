package model

import model.events.EventManager
import model.events.GameEvent

case class Player(id: String, stats: Stats = Stats(0, 0, 3)) {
  def takeDamage: Player = {
    val updatedStats = stats.updateHealth(-1)
    if (updatedStats.health <= 0)
      EventManager.notify(GameEvent.OnPlayerDeathEvent(this))
    copy(stats = updatedStats)
  }
}
