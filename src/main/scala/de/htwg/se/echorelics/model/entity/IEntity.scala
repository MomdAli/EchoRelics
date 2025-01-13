package model.entity

import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import model.entity.entityImpl.{Player, Relic, Wall, Echo}
import model.item.{ICard, IInventory}
import modules.EchorelicsModule
import utils.Stats

trait IEntity {

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
}

object IEntity {

  def isPlayer(entity: IEntity): Boolean = entity.isInstanceOf[Player]
  def isRelic(entity: IEntity): Boolean = entity.isInstanceOf[Relic]
  def isWall(entity: IEntity): Boolean = entity.isInstanceOf[Wall]
  def isEcho(entity: IEntity): Boolean = entity.isInstanceOf[Echo]
}
