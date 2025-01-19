package model.entity

import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._
import play.api.libs.json.{JsObject, Json, JsDefined}
import scala.util.{Try, Failure, Success}
import scala.xml.NodeSeq

import model.entity.entityImpl.{Player, Relic, Wall, Echo}
import model.item.{ICard, IInventory}
import modules.EchorelicsModule
import utils.{Serializable, Stats}
import utils.Deserializable
import scala.xml.Node

case class Empty() extends IEntity {

  override def toXml: Node = <entity type="empty"></entity>

  override def toJson: JsObject = JsObject.empty

  override def id: String = " "
  override def isWalkable: Boolean = true
  override def isCollectable: Boolean = false
}

trait IEntity extends Serializable[IEntity] {

  val injector = Guice.createInjector(new EchorelicsModule)

  def id: String
  def isWalkable: Boolean
  def isCollectable: Boolean

  def score: Int = 0
  def stats: Stats = Stats(0, 0, 0)
  def inventory: IInventory = injector.instance[IInventory]
  def isFull: Boolean = false

  def updateScore(score: Int): IEntity = this
  def collectCard: Option[ICard] = None
  def addCard(card: ICard): Unit = {}

  def heal: IEntity = this
  def takeDamage: IEntity = this

  def setInventory(inventory: IInventory): IEntity = this
}

object IEntity extends Deserializable[IEntity] {

  def isPlayer(entity: IEntity): Boolean = entity.isInstanceOf[Player]
  def isRelic(entity: IEntity): Boolean = entity.isInstanceOf[Relic]
  def isWall(entity: IEntity): Boolean = entity.isInstanceOf[Wall]
  def isEcho(entity: IEntity): Boolean = entity.isInstanceOf[Echo]
  def empty: IEntity = Empty(): IEntity

  override def fromXml(node: scala.xml.Node): Try[IEntity] = Try {
    val entityType = (node \ "@type").text
    entityType match {
      case "player" =>
        Player(
          id = (node \ "id").text,
          stats = Stats.fromXml((node \ "stats").head).get,
          inventory = IInventory.fromXml((node \ "inventory").head).get
        )
      case "relic" => Relic()
      case "wall"  => Wall()
      case "echo" =>
        Echo(
          id = (node \ "id").text,
          owner = fromXml((node \ "owner").head).get.asInstanceOf[Player]
        )
      case "empty" => IEntity.empty
      case _ =>
        throw new IllegalArgumentException(s"Unknown entity type: $entityType")
    }
  }

  override def fromJson(json: JsObject): Try[IEntity] = {
    val entityType = (json \ "type").as[String]
    entityType match {
      case "player" =>
        val id = (json \ "id").as[String]
        val stats = Stats.fromJson((json \ "stats").as[JsObject]).get
        val inventory =
          IInventory.fromJson((json \ "inventory").as[JsObject]).get
        Success(Player(id = id, stats = stats, inventory = inventory))
      case "relic" => Success(Relic())
      case "wall"  => Success(Wall())
      case "echo" =>
        val id = (json \ "id").as[String]
        val owner =
          fromJson((json \ "owner").as[JsObject]).get.asInstanceOf[Player]
        Success(Echo(id = id, owner = owner))
      case "empty" =>
        Success(IEntity.empty)
      case _ =>
        val errorMsg = s"Unknown entity type: $entityType"
        println(errorMsg)
        Failure(new IllegalArgumentException(errorMsg))
    }
  }
}
