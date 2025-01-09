package model.item

import service.IGameManager
import model.item.itemImpl._

enum Rarity(val value: Int):
  case Common extends Rarity(250)
  case Uncommon extends Rarity(450)
  case Rare extends Rarity(600)
  case Epic extends Rarity(700)
  case Legendary extends Rarity(750)

trait ICard {
  val rarity: Rarity
  val name: String
  val description: String
  def play(gameManager: IGameManager): IGameManager

  def toScore: Int = {
    rarity.value - 200
  }
}

object CardProvider {
  def cards: List[ICard] = List(
    HealCard(),
    StrikeCard(),
    SwapPlayerCard(),
    TimeTravelCard()
  )
}
