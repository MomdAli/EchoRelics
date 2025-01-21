package model.entity.entityImpl

import com.google.inject.Inject
import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions._
import play.api.libs.json.{Json, JsObject}

import model.events.{EventManager, GameEvent}
import model.item.IInventory
import model.entity.IEntity
import model.item.ICard
import modules.EchorelicsModule
import utils.Stats

case class Player @Inject() (
    id: String,
    override val stats: Stats = Stats(0, 1, 3),
    override val inventory: IInventory =
      Guice.createInjector(new EchorelicsModule).instance[IInventory]
) extends IEntity {

  override def isWalkable: Boolean = false
  override def isCollectable: Boolean = false
  override def score: Int = stats.score
  override def updateStats(newStats: Stats): IEntity = {
    copy(stats = newStats)
  }
  override def updateScore(s: Int): IEntity =
    copy(stats = stats.copy(score = s))
  override def addCard(card: ICard): Unit = inventory.addCard(card)
  override def isFull: Boolean = inventory.isFull

  override def toXml: scala.xml.Node = {
    <entity type="player">
      <id>{id}</id>
      {stats.toXml}
      {inventory.toXml}
    </entity>
  }

  override def toJson: JsObject = {
    Json.obj(
      "type" -> "player",
      "id" -> id,
      "stats" -> stats.toJson,
      "inventory" -> inventory.toJson
    )
  }
}
