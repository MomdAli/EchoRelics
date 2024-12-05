package model.item

import service.GameManager
import controller.Controller
import model.events.{EventManager, GameEvent}

case class TimeTravelCard() extends Card {

  override val rarity: Rarity = Rarity.Uncommon

  override val name: String = "Time Travel"

  override val description: String = "Let's you go back in time."

  override def play(gameManager: GameManager): GameManager = {
    EventManager.notify(GameEvent.OnTimeTravelEvent(6))
    gameManager
  }

}
