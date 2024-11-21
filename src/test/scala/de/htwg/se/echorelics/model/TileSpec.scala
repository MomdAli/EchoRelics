package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class TileSpec extends AnyWordSpec with Matchers {

    "A Tile" should {
        "be walkable if it is empty" in {
            val tile = Tile(TileContent.Empty)
            tile.isWalkable should be(true)
        }

        "not be walkable if it is a wall" in {
            val tile = Tile(TileContent.Wall)
            tile.isWalkable should be(false)
        }

        "not be walkable if it contains a player" in {
            val tile = Tile(TileContent.Player("P1"))
            tile.isWalkable should be(false)
        }

        "not be walkable if it is out" in {
            val tile = Tile(TileContent.Out)
            tile.isWalkable should be(false)
        }

        "contain a player if it has a player" in {
            val tile = Tile(TileContent.Player("P1"))
            tile.hasPlayer should be(true)
        }

        "not contain a player if it is empty" in {
            val tile = Tile(TileContent.Empty)
            tile.hasPlayer should be(false)
        }

        "not contain a player if it is a wall" in {
            val tile = Tile(TileContent.Wall)
            tile.hasPlayer should be(false)
        }

        "not contain a player if it is out" in {
            val tile = Tile(TileContent.Out)
            tile.hasPlayer should be(false)
        }

        "identify the correct player" in {
            val player = Player("P1")
            val tile = Tile(TileContent.Player("P1"))
            tile.isPlayer(player) should be(true)
        }

        "not identify a different player" in {
            val player = Player("P1")
            val tile = Tile(TileContent.Player("P2"))
            tile.isPlayer(player) should be(false)
        }
    }
}