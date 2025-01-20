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

    if (randomValue < 50) { // 50% chance to get a card
      val rarityValue =
        rng
          .nextInt(100)
          ._1 // Generate a value between 0 and 99 to determine rarity
      val rarity = Rarity.values
        .find(r => rarityValue < r.probability)
        .getOrElse(Rarity.Common) // Determine rarity based on probability

      val cardsOfRarity = ICard.cards.filter(_.rarity == rarity)
      if (cardsOfRarity.nonEmpty) {
        Some(
          cardsOfRarity(rng.nextInt(cardsOfRarity.size)._1)
        ) // Get a random card of the determined rarity
      } else {
        None
      }
    } else {
      None // 50% chance to get no card
    }
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
