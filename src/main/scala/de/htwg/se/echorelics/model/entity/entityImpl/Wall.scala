package model.entity.entityImpl

import model.entity.IEntity
import play.api.libs.json.{Json, JsObject}

case class Wall() extends IEntity {

  override def id: String = "#"
  override def isWalkable: Boolean = false
  override def isCollectable: Boolean = false

  override def toXml: scala.xml.Node = {
    <entity type="wall">
      <id>{id}</id>
    </entity>
  }

  override def toJson: JsObject = {
    Json.obj(
      "type" -> "wall",
      "id" -> id
    )
  }
}
