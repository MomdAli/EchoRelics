package model.item

import model.item.inventoryImpl.Inventory
import utils.{Deserializable, Serializable}
import play.api.libs.json.{JsObject, JsNull, Json, JsValue}
import scala.util.Try

trait IInventory extends Serializable[IInventory] {
  def addCard(card: ICard): Boolean
  def setCard(card: ICard): IInventory
  def removeCard(index: Int): Boolean
  def cardAt(index: Int): Option[ICard]
  def isFull: Boolean

  override def toXml = {
    <inventory>
      {
      for (i <- 0 until 3) yield {
        cardAt(i) match {
          case Some(card) => card.toXml
          case None       => <empty/>
        }
      }
    }
    </inventory>
  }

  override def toJson = {
    Json.obj(
      "cards" -> {
        for (i <- 0 until 3) yield {
          cardAt(i) match {
            case Some(card) => card.toJson
            case None       => JsNull
          }
        }
      }
    )
  }
}

object IInventory extends Deserializable[IInventory] {
  def apply(): IInventory = new Inventory()

  override def fromXml(node: scala.xml.Node): Try[IInventory] = Try {
    val inventory = IInventory()
    (node \ "inventory" \ "card").foreach { cardNode =>
      val card = ICard.fromXml(cardNode).get
      inventory.addCard(card)
    }
    inventory
  }

  override def fromJson(json: JsObject): Try[IInventory] = Try {
    val inventory = IInventory()
    (json \ "cards").as[List[JsValue]].zipWithIndex.foreach {
      case (JsNull, _) => // Do nothing for null values
      case (cardJson, _) =>
        val card = ICard.fromJson(cardJson.as[JsObject]).get
        inventory.addCard(card)
    }
    inventory
  }
}
