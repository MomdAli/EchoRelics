package de.htwg.se.echorelics.utils

import de.htwg.se.echorelics.core.{Grid, Tile}
import de.htwg.se.echorelics.model.{Echo, Player, Relic}

object DisplayRenderer {

  def render(grid: Grid): Unit = {
    val rowSeparator = createRowSeparator(grid.width)

    grid.tiles.grouped(grid.width).foreach { row =>
      println(rowSeparator)

      print("|")
      row.foreach { tile =>
        print(renderTileContent(tile) + "|")
      }
      println()
    }

    println(rowSeparator)
  }

  def createRowSeparator(width: Int): String = {
    "+" + ("---+" * width)
  }

  def renderTileContent(tile: Tile): String = {
    tile.content match {
      case Some(_: Player) => " P "
      case Some(_: Relic)  => " R "
      case Some(_: Echo)   => " E "
      case _               => "   "
    }
  }
}
