package model.item

import play.api.libs.json.{JsObject, JsNull, Json, JsString, JsValue}
import scala.util.Try

import model.item.itemImpl._
import service.IGameManager
import utils.{Deserializable, Serializable}

enum Rarity(val value: Int, val probability: Int) {

  case Common extends Rarity(50, 20)
  case Uncommon extends Rarity(250, 15)
  case Rare extends Rarity(400, 10)
  case Epic extends Rarity(500, 4)
  case Legendary extends Rarity(550, 1)
}

object Rarity {
  def withName(name: String): Rarity = name match {
    case "Common"    => Rarity.Common
    case "Uncommon"  => Rarity.Uncommon
    case "Rare"      => Rarity.Rare
    case "Epic"      => Rarity.Epic
    case "Legendary" => Rarity.Legendary
  }
}

trait ICard extends Serializable[ICard] {
  val rarity: Rarity
  val name: String
  val description: String
  def play(gameManager: IGameManager): IGameManager

  def toScore: Int = {
    rarity.value
  }

  override def toXml = {
    <card>
      <name>{name}</name>
    </card>
  }

  override def toJson = {
    Json.obj(
      "name" -> name
    )
  }
}

object ICard extends Deserializable[ICard] {
  def cards: List[ICard] = List(
    HealCard(),
    SwapPlayerCard(),
    TimeTravelCard(),
    StrikeCard()
  )

  override def fromXml(node: scala.xml.Node): Try[ICard] = Try {
    cards.find(_.name == (node \ "name").text).get
  }

  override def fromJson(json: JsObject): Try[ICard] = {
    Try {
      cards.find(_.name == (json \ "name").as[String]).get
    }
  }
}
