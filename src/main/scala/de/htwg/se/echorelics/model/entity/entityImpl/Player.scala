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
    override val stats: Stats = Stats(0, 0, 3),
    override val inventory: IInventory =
      Guice.createInjector(new EchorelicsModule).instance[IInventory]
) extends IEntity {
  override def takeDamage: Player = {
    val updatedStats = stats.updateHealth(-1)
    if (updatedStats.health <= 0)
      EventManager.notify(GameEvent.OnPlayerDeathEvent(this))
    copy(stats = updatedStats)
  }

  override def heal: Player = {
    copy(stats = stats.updateHealth(1))
  }

  override def isWalkable: Boolean = false
  override def isCollectable: Boolean = false
  override def score: Int = stats.score
  override def updateScore(score: Int): IEntity =
    copy(stats = stats.updateScore(score))
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
