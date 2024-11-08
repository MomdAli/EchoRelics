package de.htwg.se.echorelics.utils

import de.htwg.se.echorelics.core.{Grid, Tile, TileContent}
import de.htwg.se.echorelics.math.Position

object DisplayRenderer {
  def render(grid: Grid): String = {
    val horizontal = horizontalLine(grid.width)

    val rows = for (y <- 0 until grid.height) yield {
      val row = for (x <- 0 until grid.width) yield {
        val tile = grid.getTileAt(Position(x, y))
        s"|${renderTile(tile)}"
      }
      row.mkString + "|"
    }

    (horizontal +: rows.flatMap(row => Seq(row, horizontal))).mkString("\n")
  }

  def horizontalLine(width: Int): String = {
    val line = "+---" * width + "+"
    line
  }

  def renderTile(tile: Tile): String = {
    tile.content match {
      case TileContent.Empty  => "   "
      case TileContent.Wall   => " # "
      case TileContent.Player => s" P "
    }
  }
}
