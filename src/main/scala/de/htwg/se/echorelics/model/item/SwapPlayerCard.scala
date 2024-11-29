package model.item

import service.GameManager
import model.generator.Random
import service.RunningManager
import model.events.GameEvent

case class SwapPlayerCard() extends Card {
  override val rarity = Rarity.Rare
  override val name = "Swap Player"
  override val description = "Swap position with another player."

  override def play(gameManager: GameManager): GameManager = {
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
      return RunningManager(
        gameManager.move,
        gameManager.players,
        gameManager.grid,
        GameEvent.OnErrorEvent("Could not find player to swap with.")
      )
    }

    val newGrid = gameManager.grid.swap(pos1.get, pos2.get)

    RunningManager(
      gameManager.move + 1,
      gameManager.players,
      newGrid,
      GameEvent.OnPlayCardEvent(this)
    )
  }
}
