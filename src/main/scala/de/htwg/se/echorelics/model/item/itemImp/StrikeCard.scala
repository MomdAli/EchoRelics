package model.item.itemImpl

import service.IGameManager
import model.events.GameEvent
import model.item.{ICard, Rarity}

case class StrikeCard() extends ICard {
  override val rarity = Rarity.Legendary
  override val name = "Strike"
  override val description = "Deal 1 damage to all other players."

  override def play(gameManager: IGameManager): IGameManager = {
    val otherPlayers =
      gameManager.players.filterNot(_ == gameManager.currentPlayer)

    if (otherPlayers.isEmpty) {
      return gameManager
    }

    val updatedPlayers = gameManager.players.map { player =>
      if (player == gameManager.currentPlayer) player
      else player.takeDamage
    }

    gameManager.change(
      gameManager.move + 1,
      updatedPlayers,
      gameManager.grid,
      gameManager.event
    )
  }
}
