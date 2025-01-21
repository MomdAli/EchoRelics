package model.entity.entityImpl

import com.google.inject.Inject
import play.api.libs.json.{Json, JsObject}

import model.entity.IEntity
import model.events.{GameEvent, EventListener, EventManager}
import utils.Stats
import com.google.inject.name.Named

case class Echo @Inject() (
    id: String = "e",
    override val owner: String = ""
) extends IEntity {

  override def isWalkable: Boolean = false
  override def isCollectable: Boolean = false

  override def withOwner(id: String): IEntity = copy(owner = id)

  override def toXml: scala.xml.Node = {
    <entity type="echo">
      <id>{id}</id>
      <owner>{owner}</owner>
    </entity>
  }

  override def toJson: JsObject = {
    Json.obj(
      "type" -> "echo",
      "id" -> id,
      "owner" -> owner
    )
  }
}
