package model

import play.api.libs.json.{JsObject, JsValue, Json, JsNull}
import scala.util.{Try, Success, Failure}

import model.tileImpl.Tile
import model.entity.{IEntity, Empty}
import utils.{Deserializable, Serializable}

trait ITile extends Serializable[ITile] {

  val entity: Option[IEntity]

  def isWalkable: Boolean
  def hasPlayer: Boolean
  def hasWall: Boolean
  def hasRelic: Boolean
  def isPlayer(player: IEntity): Boolean
  def isEmpty: Boolean

  override def toXml: scala.xml.Node = {
    <tile>
      {entity.map(_.toXml).getOrElse(<entity type="empty"></entity>)}
    </tile>
  }

  override def toJson = {
    entity.map(_.toJson).getOrElse(Json.obj())
  }
}

object ITile extends Deserializable[ITile] {
  val emptyTile: Tile = Tile(None)
  def spawnEntity(entity: IEntity): Tile = Tile(Some(entity))
  def apply(entity: Option[IEntity]): ITile = Tile(entity)

  override def fromXml(node: scala.xml.Node): Try[ITile] = Try {
    val entityNode = (node \ "entity").head
    val entity = IEntity.fromXml(entityNode).get
    Tile(Some(entity))
  }

  override def fromJson(json: JsObject): Try[ITile] = Try {
    val entity = (json \ "type")
      .asOpt[String]
      .map { entityJson =>
        IEntity.fromJson(json)
      }
      .getOrElse {
        Success(Empty())
      }
    Tile(entity.toOption)
  }
}
