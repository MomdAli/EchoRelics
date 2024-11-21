package utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Tile, TileContent}

class DisplayRendererSpec extends AnyWordSpec with Matchers {
    "A DisplayRenderer" should {
        "render an empty grid correctly" in {
            val grid = Grid(Vector(
                Vector(Tile(TileContent.Empty), Tile(TileContent.Empty)),
                Vector(Tile(TileContent.Empty), Tile(TileContent.Empty))
            ))
            val expected = 
                """+---+---+
                  ||   |   |
                  +---+---+
                  ||   |   |
                  +---+---+""".stripMargin
            DisplayRenderer.render(grid) should be(expected)
        }

        "render a grid with a player correctly" in {
            val grid = Grid(Vector(
                Vector(Tile(TileContent.Player("1")), Tile(TileContent.Empty)),
                Vector(Tile(TileContent.Empty), Tile(TileContent.Empty))
            ))
            val expected = 
                """+---+---+
                  || 1 |   |
                  +---+---+
                  ||   |   |
                  +---+---+""".stripMargin
            DisplayRenderer.render(grid) should be(expected)
        }

        "render a grid with a wall correctly" in {
            val grid = Grid(Vector(
                Vector(Tile(TileContent.Wall), Tile(TileContent.Empty)),
                Vector(Tile(TileContent.Empty), Tile(TileContent.Empty))
            ))
            val expected = 
                """+---+---+
                  || # |   |
                  +---+---+
                  ||   |   |
                  +---+---+""".stripMargin
            DisplayRenderer.render(grid) should be(expected)
        }

        "render a grid with an out tile correctly" in {
            val grid = Grid(Vector(
                Vector(Tile(TileContent.Out), Tile(TileContent.Empty)),
                Vector(Tile(TileContent.Empty), Tile(TileContent.Empty))
            ))
            val expected = 
                """+---+---+
                  || X |   |
                  +---+---+
                  ||   |   |
                  +---+---+""".stripMargin
            DisplayRenderer.render(grid) should be(expected)
        }

        "render a larger grid correctly" in {
            val grid = Grid(Vector(
                Vector(Tile(TileContent.Empty), Tile(TileContent.Player("1")), Tile(TileContent.Empty)),
                Vector(Tile(TileContent.Wall), Tile(TileContent.Empty), Tile(TileContent.Out)),
                Vector(Tile(TileContent.Empty), Tile(TileContent.Empty), Tile(TileContent.Empty))
            ))
            val expected = 
                """+---+---+---+
                  ||   | 1 |   |
                  +---+---+---+
                  || # |   | X |
                  +---+---+---+
                  ||   |   |   |
                  +---+---+---+""".stripMargin
            DisplayRenderer.render(grid) should be(expected)
        }

        "render a grid with multiple players correctly" in {
            val grid = Grid(Vector(
                Vector(Tile(TileContent.Player("1")), Tile(TileContent.Player("2"))),
                Vector(Tile(TileContent.Player("3")), Tile(TileContent.Player("4")))
            ))
            val expected = 
                """+---+---+
                  || 1 | 2 |
                  +---+---+
                  || 3 | 4 |
                  +---+---+""".stripMargin
            DisplayRenderer.render(grid) should be(expected)
        }
    }
}
