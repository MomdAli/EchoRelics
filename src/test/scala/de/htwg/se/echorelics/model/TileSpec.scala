package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TileSpec extends AnyWordSpec with Matchers {

  "A Tile" should {

    "be created with Empty content" in {
      val tile = Tile(TileContent.Empty)
      tile.content should be(TileContent.Empty)
    }

    "be created with Wall content" in {
      val tile = Tile(TileContent.Wall)
      tile.content should be(TileContent.Wall)
    }

    "be created with Player content" in {
      val player = Player("1")
      val tile = Tile(TileContent.Player(player.id))
      tile.content should be(TileContent.Player(player.id))
    }

    "be walkable if it is empty" in {
      val tile = Tile(TileContent.Empty)
      tile.isWalkable should be(true)
    }

    "not be walkable if it is a wall" in {
      val tile = Tile(TileContent.Wall)
      tile.isWalkable should be(false)
    }

    "contain a player if it has player content" in {
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

    "create an empty tile using the companion object" in {
      val tile = Tile.EmptyTile
      tile.content should be(TileContent.Empty)
    }

    "create a wall tile using the companion object" in {
      val tile = Tile.WallTile
      tile.content should be(TileContent.Wall)
    }

    "create a player tile using the companion object" in {
      val player = Player("1")
      val tile = Tile.PlayerTile(player)
      tile.content should be(TileContent.Player(player.id))
    }
  }
}
