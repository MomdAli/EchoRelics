package model.entity.entityImpl

import play.api.libs.json.{Json, JsObject}

import model.entity.IEntity
import model.item.ICard
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
    val (index, _) = rng.nextInt(ICard.cards.size)
    val card = ICard.cards(index)

    val rarityChance = card.rarity.value
    val (randomValue, _) = rng.nextInt(1000)

    if (randomValue < rarityChance) Some(card) else None
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
