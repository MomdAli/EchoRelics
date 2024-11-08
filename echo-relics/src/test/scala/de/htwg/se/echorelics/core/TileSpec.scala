package de.htwg.se.echorelics.core

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

    "contain a player if it has a player" in {
      val tile = Tile(TileContent.Player)
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
  }

  "The Tile object" should {
    "create an empty tile" in {
      val tile = Tile.EmptyTile
      tile.content should be(TileContent.Empty)
    }

    "create a wall tile" in {
      val tile = Tile.WallTile
      tile.content should be(TileContent.Wall)
    }

    "create a player tile" in {
      val tile = Tile.PlayerTile("player1")
      tile.content should be(TileContent.Player)
    }
  }
}
