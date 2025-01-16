package model.item

import play.api.libs.json.{JsObject, JsNull, Json, JsString, JsValue}
import scala.util.Try

import model.item.itemImpl._
import service.IGameManager
import utils.{Deserializable, Serializable}

enum Rarity(val value: Int) {

  case Common extends Rarity(250)
  case Uncommon extends Rarity(450)
  case Rare extends Rarity(600)
  case Epic extends Rarity(700)
  case Legendary extends Rarity(750)

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
    rarity.value - 200
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
    TimeTravelCard()
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
