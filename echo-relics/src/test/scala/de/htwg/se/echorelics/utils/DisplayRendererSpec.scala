package de.htwg.se.echorelics.utils

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.echorelics.core.Tile
import de.htwg.se.echorelics.model.{Echo, Player, Relic}
import de.htwg.se.echorelics.math.Position


class DisplayRendererSpec extends AnyWordSpec {
  "A DisplayRenderer" when {
    "rendering a grid" should {
      "have a scalable bar" in {
        DisplayRenderer.createRowSeparator(3) shouldBe "+---+---+---+"
        DisplayRenderer.createRowSeparator(4) shouldBe "+---+---+---+---+"
      }

      "render the content of the tile" in {
        val pos = Position(0, 0)
        DisplayRenderer.renderTileContent(Tile(pos, None)) shouldBe "   "

        DisplayRenderer.renderTileContent(
          Tile(pos, Some(Player("Player", pos)))
        ) shouldBe " P "

        DisplayRenderer.renderTileContent(
          Tile(pos, Some(Relic.defaultRelic()))
        ) shouldBe " R "

        DisplayRenderer.renderTileContent(
          Tile(pos, Some(Echo("Echo1", pos)))
        ) shouldBe " E "
      }
    }
  }
}
