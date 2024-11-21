package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import utils.{Position, Direction}

class GridSpec extends AnyWordSpec with Matchers {

    "A Grid" should {

        "be created with a given size" in {
            val grid = new Grid(3)
            grid.size should be(3)
            grid.tiles.flatten.forall(_.content == TileContent.Empty) should be(true)
        }

        "allow setting a tile at a specific position" in {
            val grid = new Grid(3)
            val newGrid = grid.set(Position(1, 1), Tile(TileContent.Player("1")))
            newGrid.tileAt(Position(1, 1)).content should be(TileContent.Player("1"))
        }

        "detect out of bounds positions" in {
            val grid = new Grid(3)
            grid.isOutOfBounds(Position(-1, 0)) should be(true)
            grid.isOutOfBounds(Position(0, -1)) should be(true)
            grid.isOutOfBounds(Position(3, 0)) should be(true)
            grid.isOutOfBounds(Position(0, 3)) should be(true)
            grid.isOutOfBounds(Position(1, 1)) should be(false)
        }

        "find a player on the grid" in {
            val grid = new Grid(3).set(Position(1, 1), Tile(TileContent.Player("1")))
            grid.findPlayer(Player("1")) should be(Position(1, 1))
        }

        "move a player in a given direction" in {
            val grid = new Grid(3).set(Position(1, 1), Tile(TileContent.Player("1")))
            val newGrid = grid.movePlayer(Player("1"), Direction.Right)
            newGrid.tileAt(Position(1, 1)).content should be(TileContent.Empty)
            newGrid.tileAt(Position(2, 1)).content should be(TileContent.Player("1"))
        }

        "not move a player out of bounds" in {
            val grid = new Grid(3).set(Position(2, 2), Tile(TileContent.Player("1")))
            val newGrid = grid.movePlayer(Player("1"), Direction.Right)
            newGrid should be(grid)
        }

        "not move a player to a non-walkable tile" in {
            val grid = new Grid(3)
                .set(Position(1, 1), Tile(TileContent.Player("1")))
                .set(Position(2, 1), Tile(TileContent.Wall))
            val newGrid = grid.movePlayer(Player("1"), Direction.Right)
            newGrid should be(grid)
        }

        "setup grid with players at random positions" in {
            val players = List(Player("1"), Player("2"))
            val grid = new Grid(3).setupGrid(players)
            players.foreach { player =>
                val position = grid.findPlayer(player)
                grid.tileAt(position).content should be(TileContent.Player(player.id))
            }
        }

        "render the grid as a string" in {
            val grid = new Grid(3).set(Position(1, 1), Tile(TileContent.Player("P")))
            val rendered = grid.toString
            rendered should include("P")
        }
    }
}