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
            val player = Player("1")
            val tile = Tile(TileContent.Player(player.id))
            tile.isWalkable should be(false)
        }

        "not be walkable if it is out of bounds" in {
            val tile = Tile(TileContent.Out)
            tile.isWalkable should be(false)
        }

        "contain a player if it has a player" in {
            val player = Player("1")
            val tile = Tile(TileContent.Player(player.id))
            tile.hasPlayer should be(true)
        }

        "not contain a player if it is empty" in {
            val tile = Tile(TileContent.Empty)
            tile.hasPlayer should be(false)
        }

        "identify the correct player" in {
            val player = Player("1")
            val tile = Tile(TileContent.Player(player.id))
            tile.isPlayer(player) should be(true)
        }

        "not identify a different player" in {
            val player1 = Player("1")
            val player2 = Player("2")
            val tile = Tile(TileContent.Player(player1.id))
            tile.isPlayer(player2) should be(false)
        }
    }
}