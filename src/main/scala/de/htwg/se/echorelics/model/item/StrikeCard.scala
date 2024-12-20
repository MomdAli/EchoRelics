package model.item

import service.{GameManager, RunningManager}
import model.events.GameEvent

case class StrikeCard() extends Card {
  override val rarity = Rarity.Legendary
  override val name = "Strike"
  override val description = "Deal 1 damage to all other players."

  override def play(gameManager: GameManager): GameManager = {
    val otherPlayers =
      gameManager.players.filterNot(_ == gameManager.currentPlayer)

    if (otherPlayers.isEmpty) {
      return gameManager
    }

    val updatedPlayers = gameManager.players.map { player =>
      if (player == gameManager.currentPlayer) player
      else player.takeDamage
    }

    RunningManager(
      gameManager.move + 1,
      updatedPlayers,
      gameManager.grid,
      GameEvent.OnPlayCardEvent(this)
    )
  }
}
