package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.entity.entityImpl.{Player, Echo, Relic, Wall}
import model.tileImpl.Tile
import model.gridImpl.Grid
import utils.{Position, Direction}
import model.{ITile, IGrid}
import model.entity.IEntity

class GridSpec extends AnyWordSpec with Matchers {

  "A Grid" should {

    "have a correct size and initialize tiles properly" in {
      val grid = new Grid(5)
      grid.size shouldBe 5
      grid.tiles.length shouldBe 5
      grid.tiles.forall(_.length == 5) shouldBe true
    }

    "return the correct tile using tileAt" in {
      val grid = new Grid(3)
      val tile = grid.tileAt(Position(1,1))
      tile.isEmpty shouldBe true

      grid.tileAt(Position(-1,0)).isEmpty shouldBe true // Out of bounds -> emptyTile
      grid.tileAt(Position(3,3)).isEmpty shouldBe true  // Out of bounds -> emptyTile
    }

    "update tile with set" in {
      val grid = new Grid(2)
      val player = Player("p1")
      val updated = grid.set(Position(0, 0), ITile(Some(player)))
      updated.tileAt(Position(0,0)).entity.exists(_.id == "p1") shouldBe true
    }

    "check isOutOfBounds correctly" in {
      val grid = new Grid(2)
      grid.isOutOfBounds(Position(-1,0)) shouldBe true
      grid.isOutOfBounds(Position(2,2)) shouldBe true
      grid.isOutOfBounds(Position(1,1)) shouldBe false
    }

    "find a player using findPlayer" in {
      val player = Player("p2")
      val grid = new Grid(2).set(Position(1,1), ITile(Some(player)))
      val found = grid.findPlayer(player)
      found shouldBe Some(Position(1,1))

      val notFound = grid.findPlayer(Player("doesNotExist"))
      notFound shouldBe None
    }

    "move a player properly with movePlayer" in {
      val player = Player("p3")
      val grid = new Grid(3).set(Position(1,1), ITile(Some(player)))
      // Move Up
      val movedUp = grid.movePlayer(player, Direction.Up)
      movedUp.tileAt(Position(1,0)).entity.exists(_.id == "p3") shouldBe true
      movedUp.tileAt(Position(1,1)).isEmpty shouldBe true

      // Attempt to move out of bounds (should stay put)
      val movedOutOfBounds = movedUp.movePlayer(player, Direction.Up)
      movedOutOfBounds.tileAt(Position(1,0)).entity.exists(_.id == "p3") shouldBe true
    }

    "spawn an echo with spawnEcho" in {
      val echo = Echo("e1", "pX")
      val grid = new Grid(3)
      val spawned = grid.spawnEcho(Position(1,1), echo)
      // We can't predict exactly which position because spawnEcho tries random directions,
      // but we check that it set the echo somewhere inside the grid (unless all blocked).
      val possiblePositions =
        for {
          y <- 0 until 3
          x <- 0 until 3
          if spawned.tileAt(Position(x, y)).entity.contains(echo)
        } yield Position(x, y)
      // If there's empty space around (1,1), we expect at least one position with echo
      possiblePositions.nonEmpty shouldBe true
    }

    "move echoes with moveEchoes" in {
      val echo = Echo("e2", "ownerX")
      val grid = new Grid(3).set(Position(1,1), ITile(Some(echo)))
      val moved = grid.moveEchoes
      // Echo either stays or moves, but it won't vanish unless it collides with a player or wall
      val echoPositions =
        (for {
          y <- 0 until 3
          x <- 0 until 3
          if moved.tileAt(Position(x, y)).entity.contains(echo)
        } yield 1).sum
      echoPositions shouldBe 1
    }

    "increase its size with increaseSize up to 15" in {
      val grid = new Grid(14)
      val bigger = grid.increaseSize
      bigger.size shouldBe 15

      val tooBig = bigger.increaseSize
      tooBig.size shouldBe 15 // won't increase past 15
    }

    "decrease its size with decreaseSize down to 10" in {
      val grid = new Grid(11)
      val smaller = grid.decreaseSize
      smaller.size shouldBe 10

      val tooSmall = smaller.decreaseSize
      tooSmall.size shouldBe 10 // won't go below 10
    }

    "swap two tiles with swap" in {
      val player1 = Player("p1")
      val player2 = Player("p2")
      val grid = new Grid(2)
        .set(Position(0,0), ITile(Some(player1)))
        .set(Position(1,1), ITile(Some(player2)))

      val swapped = grid.swap(Position(0,0), Position(1,1))
      swapped.tileAt(Position(1,1)).entity.exists(_.id == "p1") shouldBe true
      swapped.tileAt(Position(0,0)).entity.exists(_.id == "p2") shouldBe true
    }
  }

  "IGrid trait" should {
    "be satisfied by our Grid implementation for all required methods" in {
      val grid: IGrid = new Grid(4)
      grid.size shouldBe 4
      grid.set(Position(0,0), ITile.emptyTile).size shouldBe 4
      grid.tileAt(Position(3,3)).isEmpty shouldBe true
      grid.isOutOfBounds(Position(-1,3)) shouldBe true
      grid.findPlayer(Player("dummy")) shouldBe None
      grid.moveEchoes.size shouldBe 4
      val echo = Echo("echo-test", "owner")
      val echoSpawned = grid.spawnEcho(Position(2,2), echo)
      echoSpawned.size shouldBe 4
      echoSpawned.tileAt(Position(2,2)).isEmpty shouldBe true // spawn logic may shift it
      grid.increaseSize.size shouldBe 5
      grid.decreaseSize.size shouldBe 4
      val swapped = grid.swap(Position(0,0), Position(1,1))
      swapped.size shouldBe 4
    }
  }
}