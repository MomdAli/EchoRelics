package model.item

import service.GameManager

enum Rarity(val value: Int):
  case Common extends Rarity(250)
  case Uncommon extends Rarity(450)
  case Rare extends Rarity(600)
  case Epic extends Rarity(700)
  case Legendary extends Rarity(750)

trait Card {
  val rarity: Rarity
  val name: String
  val description: String
  def play(gameManager: GameManager): GameManager

  def toScore: Int = {
    rarity.value - 200
  }
}
