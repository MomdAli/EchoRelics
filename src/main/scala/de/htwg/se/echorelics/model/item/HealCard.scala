package model.item

import service.{GameManager, RunningManager}
import model.events.GameEvent

case class HealCard() extends Card {
  override val rarity = Rarity.Common
  override val name = "Heal"
  override val description = "Heal the player for 1 health point."

  override def play(gameManager: GameManager): GameManager = {
    val updatedPlayers = gameManager.players.map { player =>
      if (player == gameManager.currentPlayer) player.heal
      else player
    }

    RunningManager(
      gameManager.move + 1,
      updatedPlayers,
      gameManager.grid,
      GameEvent.OnPlayCardEvent(this)
    )
  }
}
