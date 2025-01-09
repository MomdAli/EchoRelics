package model.item.itemImpl

import service.IGameManager
import model.events.GameEvent
import model.item.{ICard, Rarity}

case class HealCard() extends ICard {
  override val rarity = Rarity.Common
  override val name = "Heal"
  override val description = "Heal the player for 1 health point."

  override def play(gameManager: IGameManager): IGameManager = {
    val updatedPlayers = gameManager.players.map { player =>
      if (player == gameManager.currentPlayer) player.heal
      else player
    }

    IGameManager.createRunningManager(
      gameManager.move + 1,
      updatedPlayers,
      gameManager.grid,
      gameManager.event
    )
  }
}
