package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.entity.{Player, Wall, Relic}

class TileSpec extends AnyWordSpec with Matchers {

  "A Tile" should {

    "be walkable if it contains no entity" in {
      val tile = Tile(None)
      tile.isWalkable should be(true)
    }

    "not be walkable if it contains a non-walkable entity" in {
      val tile = Tile(Some(Wall()))
      tile.isWalkable should be(false)
    }

    "correctly identify if it contains a player" in {
      val player = Player("player1")
      val tile = Tile(Some(player))
      tile.hasPlayer should be(true)
    }

    "correctly identify if it does not contain a player" in {
      val tile = Tile(None)
      tile.hasPlayer should be(false)
    }

    "correctly identify if it contains a wall" in {
      val tile = Tile(Some(Wall()))
      tile.hasWall should be(true)
    }

    "correctly identify if it does not contain a wall" in {
      val tile = Tile(None)
      tile.hasWall should be(false)
    }

    "correctly identify if it contains a relic" in {
      val tile = Tile(Some(Relic()))
      tile.hasRelic should be(true)
    }

    "correctly identify if it does not contain a relic" in {
      val tile = Tile(None)
      tile.hasRelic should be(false)
    }

    "correctly identify if it contains a specific player" in {
      val player = Player("player1")
      val tile = Tile(Some(player))
      tile.isPlayer(player) should be(true)
    }

    "correctly identify if it does not contain a specific player" in {
      val player1 = Player("player1")
      val player2 = Player("player2")
      val tile = Tile(Some(player1))
      tile.isPlayer(player2) should be(false)
    }

    "be empty if it contains no entity" in {
      val tile = Tile(None)
      tile.isEmpty should be(true)
    }

    "not be empty if it contains an entity" in {
      val player = Player("player1")
      val tile = Tile(Some(player))
      tile.isEmpty should be(false)
    }
  }
}
