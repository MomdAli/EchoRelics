package model.entity

import model.entity.entityImpl.{Player, Relic, Wall, Echo}
import model.item.{ICard, IInventory}
import utils.Stats

trait IEntity {
  def id: String
  def isWalkable: Boolean
  def isCollectable: Boolean

  def score: Int = 0
  def stats: Stats = Stats(0, 0, 0)
  def inventory: IInventory = IInventory()
  def isFull: Boolean = false

  def updateScore(score: Int): IEntity = this
  def collectCard: Option[ICard] = None
  def addCard(card: ICard): Unit = {}

  def heal: IEntity = this
  def takeDamage: IEntity = this
}

object IEntity {
  def createPlayer(id: String): IEntity = Player(id)
  def createRelic(): IEntity = Relic()
  def createWall(): IEntity = Wall()

  def isPlayer(entity: IEntity): Boolean = entity.isInstanceOf[Player]
  def isRelic(entity: IEntity): Boolean = entity.isInstanceOf[Relic]
  def isWall(entity: IEntity): Boolean = entity.isInstanceOf[Wall]
  def isEcho(entity: IEntity): Boolean = entity.isInstanceOf[Echo]
}
