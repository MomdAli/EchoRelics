package model.item.itemImpl

import service.IGameManager
import controller.Controller
import model.events.{EventManager, GameEvent}
import model.item.{ICard, Rarity}

case class TimeTravelCard() extends ICard {

  override val rarity: Rarity = Rarity.Uncommon

  override val name: String = "Time Travel"

  override val description: String = "Let's you go back in time."

  override def play(gameManager: IGameManager): IGameManager = {
    EventManager.notify(GameEvent.OnTimeTravelEvent(6)) // ! Hardcoded value
    gameManager
  }

}
