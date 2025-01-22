package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.entity.entityImpl.{Player, Relic, Wall, Echo}
import model.entity.Empty
import model.tileImpl.Tile

class TileSpec extends AnyWordSpec with Matchers {

  "A Tile" should {

    "be walkable if it contains no entity" in {
      val tile = Tile(None)
      tile.isWalkable should be(true)
    }

    "not be walkable if it contains a player" in {
      val player = Player(id = "player1")
      val tile = Tile(Some(player))
      tile.isWalkable should be(false)
    }

    "be walkable if it contains a relic" in {
      val relic = Relic()
      val tile = Tile(Some(relic))
      tile.isWalkable should be(true)
    }

    "not be walkable if it contains a wall" in {
      val wall = Wall()
      val tile = Tile(Some(wall))
      tile.isWalkable should be(false)
    }

    "not be walkable if it contains an echo" in {
      val echo = Echo(id = "e1", owner = "player1")
      val tile = Tile(Some(echo))
      tile.isWalkable should be(false)
    }

    "be empty if it contains no entity" in {
      val tile = Tile(None)
      tile.isEmpty should be(true)
    }

    "not be empty if it contains an entity" in {
      val player = Player(id = "player1")
      val tile = Tile(Some(player))
      tile.isEmpty should be(false)
    }

    "correctly identify if it contains a player" in {
      val player = Player(id = "player1")
      val tile = Tile(Some(player))
      tile.hasPlayer should be(true)
    }

    "correctly identify if it does not contain a player" in {
      val relic = Relic()
      val tile = Tile(Some(relic))
      tile.hasPlayer should be(false)
    }

    "correctly identify if it contains a wall" in {
      val wall = Wall()
      val tile = Tile(Some(wall))
      tile.hasWall should be(true)
    }

    "correctly identify if it does not contain a wall" in {
      val player = Player(id = "player1")
      val tile = Tile(Some(player))
      tile.hasWall should be(false)
    }

    "correctly identify if it contains a relic" in {
      val relic = Relic()
      val tile = Tile(Some(relic))
      tile.hasRelic should be(true)
    }

    "correctly identify if it does not contain a relic" in {
      val player = Player(id = "player1")
      val tile = Tile(Some(player))
      tile.hasRelic should be(false)
    }

    "correctly identify if it contains a specific player" in {
      val player = Player(id = "player1")
      val tile = Tile(Some(player))
      tile.isPlayer(player) should be(true)
    }

    "correctly identify if it does not contain a specific player" in {
      val player1 = Player(id = "player1")
      val player2 = Player(id = "player2")
      val tile = Tile(Some(player1))
      tile.isPlayer(player2) should be(false)
    }
  }
}
