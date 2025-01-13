package model.item.itemImpl

import model.events.GameEvent
import service.IGameManager
import model.item.{ICard, Rarity}
import utils.Random

case class SwapPlayerCard() extends ICard {
  override val rarity = Rarity.Rare
  override val name = "Swap Player"
  override val description = "Swap position with another player."

  override def play(gameManager: IGameManager): IGameManager = {
    val rng = Random(System.currentTimeMillis().toInt)
    val self = gameManager.currentPlayer
    val other = {
      val otherPlayers = gameManager.players.filterNot(_ == self)
      val (index, _) = rng.nextInt(otherPlayers.size)
      otherPlayers(index)
    }

    val pos1 = gameManager.grid.findPlayer(self)
    val pos2 = gameManager.grid.findPlayer(other)

    if (pos1.isEmpty || pos2.isEmpty) {
      return gameManager.change(
        gameManager.move,
        gameManager.players,
        gameManager.grid,
        GameEvent.OnErrorEvent("Could not find player to swap with.")
      )
    }

    (pos1, pos2) match {
      case (Some(p1), Some(p2)) =>
        val newGrid = gameManager.grid.swap(p1, p2)
        gameManager.change(
          gameManager.move + 1,
          gameManager.players,
          newGrid,
          GameEvent.OnPlayCardEvent(this)
        )
      case _ =>
        gameManager.change(
          gameManager.move,
          gameManager.players,
          gameManager.grid,
          GameEvent.OnErrorEvent("Could not find player to swap with.")
        )
    }
  }
}
