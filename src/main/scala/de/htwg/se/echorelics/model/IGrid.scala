package model

import utils.{Direction, Position}
import model.entity.IEntity
import model.gridImpl.Grid

trait IGrid {
  val tiles: Vector[Vector[ITile]]
  def set(position: Position, tile: ITile): IGrid
  def size: Int
  def tileAt(position: Position): ITile
  def isOutOfBounds(position: Position): Boolean
  def movePlayer(player: IEntity, direction: Direction): IGrid
  def moveEcho(echo: IEntity): IGrid
  def findPlayer(player: IEntity): Option[Position]
  def increaseSize: IGrid
  def decreaseSize: IGrid
  def swap(pos1: Position, pos2: Position): IGrid
}
