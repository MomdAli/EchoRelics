package model.item.itemImp

import service.IGameManager
import model.events.GameEvent
import model.item.{ICard, Rarity}
import utils.Random

case class KillCard() extends ICard {
  override val rarity = Rarity.Legendary
  override val name = "Kill"
  override val description = "Kill one random player."

  override def play(gameManager: IGameManager): IGameManager = {
    val rng = Random(System.currentTimeMillis().toInt)
    val otherPlayers =
      gameManager.players.filterNot(_ == gameManager.currentPlayer)
    if (otherPlayers.nonEmpty) {
      val (index, _) = rng.nextInt(otherPlayers.size)
      val playerToRemove = otherPlayers(index)
      gameManager.change(
        move = gameManager.move + 1,
        players = gameManager.players.filterNot(_ == playerToRemove),
        event = GameEvent.OnPlayCardEvent(this)
      )
    } else {
      gameManager
    }
  }
}
