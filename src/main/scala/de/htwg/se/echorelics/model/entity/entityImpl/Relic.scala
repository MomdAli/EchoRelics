package model.entity.entityImpl

import play.api.libs.json.{Json, JsObject}

import model.entity.IEntity
import model.item.{ICard, Rarity}
import utils.Random

case class Relic() extends IEntity {

  override val id: String = "$"
  override def isWalkable: Boolean = true
  override def isCollectable: Boolean = true

  override val score: Int = {
    val min = 50
    val max = 100
    val seed = System.currentTimeMillis().toInt
    val rng = Random(seed)
    val diff = (max - min) / 10 + 1
    val (score, _) = rng.nextInt(diff)
    score * 10 + min
  }

  override val collectCard: Option[ICard] = {
    val seed = System.currentTimeMillis().toInt
    val rng = Random(seed)
    val (randomValue, _) = rng.nextInt(100) // Generate a value between 0 and 99

    // First 50% chance to get any card
    if (randomValue < 50) {
      val (rarityRoll, _) = rng.nextInt(50) // Roll for rarity (0-49)

      // Determine rarity based on cumulative probabilities
      val rarity =
        if (rarityRoll < 20) Rarity.Common // 0-19 (20%)
        else if (rarityRoll < 35) Rarity.Uncommon // 20-34 (15%)
        else if (rarityRoll < 45) Rarity.Rare // 35-44 (10%)
        else if (rarityRoll < 49) Rarity.Epic // 45-48 (4%)
        else Rarity.Legendary // 49 (1%)

      // Get all cards of the chosen rarity
      val cardsOfRarity = ICard.cards.filter(_.rarity == rarity)

      if (cardsOfRarity.nonEmpty) {
        val (cardIndex, _) = rng.nextInt(cardsOfRarity.size)
        Some(cardsOfRarity(cardIndex))
      } else None
    } else None // 50% chance to get no card
  }

  override val obtainEcho: Boolean = {
    val seed = System.currentTimeMillis().toInt
    val rng = Random(seed)
    val (randomValue, _) = rng.nextInt(100)
    randomValue < 35 // 35% chance to obtain echo
  }

  override def toXml: scala.xml.Node = {
    <entity type="relic">
      <id>{id}</id>
      <score>{score}</score>
    </entity>
  }

  override def toJson: JsObject = {
    Json.obj(
      "type" -> "relic",
      "id" -> id,
      "score" -> score
    )
  }
}
