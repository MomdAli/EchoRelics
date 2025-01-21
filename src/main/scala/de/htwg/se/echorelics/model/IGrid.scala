package model

import play.api.libs.json.{JsObject, JsValue, Json}
import scala.util.{Try, Success, Failure}

import model.entity.IEntity
import model.gridImpl.Grid
import utils.{Direction, Position, Serializable, Deserializable}

trait IGrid extends Serializable[IGrid] {
  val tiles: Vector[Vector[ITile]]
  def set(position: Position, tile: ITile): IGrid
  def size: Int
  def tileAt(position: Position): ITile
  def isOutOfBounds(position: Position): Boolean
  def movePlayer(player: IEntity, direction: Direction): IGrid
  def moveEchoes: IGrid
  def spawnEcho(position: Position, echo: IEntity): IGrid
  def findPlayer(player: IEntity): Option[Position]
  def increaseSize: IGrid
  def decreaseSize: IGrid
  def swap(pos1: Position, pos2: Position): IGrid

  override def toXml: scala.xml.Node = {
    <grid size={size.toString}>
      {tiles.map(row => <row>{row.map(_.toXml)}</row>)}
    </grid>
  }

  override def toJson: JsObject = {
    Json.obj(
      "size" -> size,
      "grid" -> tiles.map(row => row.map(_.toJson))
    )
  }
}

object IGrid extends Deserializable[IGrid] {
  def apply(size: Int): IGrid = new Grid(size)

  override def fromXml(node: scala.xml.Node): Try[IGrid] = Try {
    val size = (node \ "@size").text.toInt
    val initialGrid = IGrid(size)

    val tiles = (node \ "row").map { row =>
      row.child
        .collect {
          case tileNode: scala.xml.Elem => // Only process element nodes
            val tile = ITile.fromXml(tileNode)
            tile
        }
        .collect { case scala.util.Success(tile) => tile }
        .toVector
    }.toVector

    // Accumulate grid updates
    val finalGrid = tiles.zipWithIndex.foldLeft(initialGrid) {
      (gridAcc, rowWithIndex) =>
        val (row, y) = rowWithIndex
        row.zipWithIndex.foldLeft(gridAcc) { (rowAcc, tileWithIndex) =>
          val (tile, x) = tileWithIndex
          rowAcc.set(Position(x, y), tile) // Update and return new grid
        }
    }

    finalGrid
  }
  override def fromJson(json: JsObject): Try[IGrid] = Try {
    val size = (json \ "size").as[Int]
    val initialGrid = IGrid(size)

    val tiles = (json \ "grid").as[List[List[JsObject]]]

    // Accumulate grid updates
    val finalGrid = tiles.zipWithIndex.foldLeft(initialGrid) {
      (gridAcc, rowWithIndex) =>
        val (row, y) = rowWithIndex
        row.zipWithIndex.foldLeft(gridAcc) { (rowAcc, tileWithIndex) =>
          val (tile, x) = tileWithIndex
          rowAcc.set(
            Position(x, y),
            ITile
              .fromJson(tile)
              .getOrElse(
                throw new IllegalArgumentException("Invalid tile JSON")
              )
          ) // Update and return new grid
        }
    }

    finalGrid
  }
}
