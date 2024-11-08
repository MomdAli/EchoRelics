package de.htwg.se.echorelics.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.echorelics.core.{Grid, Tile, TileContent}
import de.htwg.se.echorelics.model.Player
import de.htwg.se.echorelics.math.Position

class DisplayRendererSpec extends AnyWordSpec with Matchers {
  "A DisplayRenderer" should {
    "render a tile correctly" in {
      DisplayRenderer.renderTile(Tile(TileContent.Empty)) should be("   ")
      DisplayRenderer.renderTile(Tile(TileContent.Wall)) should be(" # ")
      DisplayRenderer.renderTile(Tile(TileContent.Player)) should be(" P ")
    }

    "create a horizontal line correctly" in {
      DisplayRenderer.horizontalLine(3) should be("+---+---+---+")
    }

    "render a grid correctly" in {
      val player = Player("P", Position(0, 0))
      val grid = Grid.DefaultGrid(player, 3, 3)
      val expectedGrid = "+---+---+---+\n| P |   |   |" +
        "\n+---+---+---+\n|   |   |   |" +
        "\n+---+---+---+\n|   |   |   |" +
        "\n+---+---+---+"
      DisplayRenderer.render(grid) should be(expectedGrid)
    }
  }
}
