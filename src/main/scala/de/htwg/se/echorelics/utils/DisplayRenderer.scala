package utils

import model.{Grid, Tile, TileContent}
import utils.Position

object DisplayRenderer {
  def render(grid: Grid): String = {
    val horizontal = horizontalLine(grid.size)

    val rows = for (y <- 0 until grid.size) yield {
      val row = for (x <- 0 until grid.size) yield {
        val tile = grid.tileAt(Position(x, y))
        s"|${renderTile(tile)}"
      }
      row.mkString + "|"
    }

    (horizontal +: rows.flatMap(row => Seq(row, horizontal)))
      .mkString("\n")
  }

  def horizontalLine(size: Int): String = {
    val line = "+---" * size + "+"
    line
  }

  def renderTile(tile: Tile): String = {
    tile.content match {
      case TileContent.Empty      => "   "
      case TileContent.Player(id) => s" ${id} "
      case TileContent.Wall       => " # "
      case TileContent.Out        => " X "
    }
  }
}
