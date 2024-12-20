package model.entity

import model.entity.Entity
import model.generator.Random
import model.item.{Card, HealCard, StrikeCard, SwapPlayerCard, TimeTravelCard}

case class Relic() extends Entity {

  override val id: String = "$"
  override def isWalkable: Boolean = true
  override def isCollectable: Boolean = true

  private val cards: List[Card] = List(
    HealCard(),
    SwapPlayerCard(),
    StrikeCard(),
    TimeTravelCard()
  )

  val score: Int = {
    val min = 50
    val max = 100
    val seed = System.currentTimeMillis().toInt
    val rng = Random(seed)
    val diff = (max - min) / 10 + 1
    val (score, _) = rng.nextInt(diff)
    score * 10 + min
  }

  val collectCard: Option[Card] = {
    val seed = System.currentTimeMillis().toInt
    val rng = Random(seed)
    val (index, _) = rng.nextInt(cards.size)
    val card = cards(index)

    val rarityChance = card.rarity.value
    val (randomValue, _) = rng.nextInt(1000)

    if (randomValue < rarityChance) Some(card) else None
  }
}
