package model.generator

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Tile}
import utils.Position
import model.entity.{Player, Relic}
import model.config.{Config, Configurator}

class GridSpawnerSpec extends AnyWordSpec with Matchers {

  "A GridSpawner" should {

    val config = Configurator().withGrid(10).withPlayer(2).build
    val grid = new Grid(10)
    val spawner = new GridSpawner(config)
    val players = Seq(Player("player1"), Player("player2"))

    "setup a starting grid with players" in {
      val newGrid = spawner.setupStartingGrid(grid, players)

      players.foreach { player =>
        newGrid.tiles.flatten.exists(_.entity.contains(player)) should be(true)
      }
    }

    "generate walls correctly" in {
      val newGrid = spawner.setupStartingGrid(grid, Seq.empty)

      newGrid.tiles.flatten.count(_.hasWall) should be > 0
    }

    "remove corner walls" in {
      val newGrid = spawner.setupStartingGrid(grid, Seq.empty)

      val corners = Set(
        Position(0, 0),
        Position(0, grid.size - 1),
        Position(grid.size - 1, 0),
        Position(grid.size - 1, grid.size - 1)
      )

      corners.foreach { corner =>
        newGrid.tileAt(corner).hasWall should be(false)
      }
    }

    "spawn relics correctly" in {
      val newGrid = spawner.setupStartingGrid(grid, Seq.empty)

      newGrid.tiles.flatten.count(
        _.entity.exists(_.isInstanceOf[Relic])
      ) should be(3)
    }

    "place players in corners" in {
      val newGrid = spawner.setupStartingGrid(grid, players)

      val corners = Seq(
        Position(0, 0),
        Position(0, grid.size - 1),
        Position(grid.size - 1, 0),
        Position(grid.size - 1, grid.size - 1)
      )

      players.zip(corners).foreach { case (player, corner) =>
        newGrid.tileAt(corner).entity.contains(player) should be(true)
      }
    }
  }
}
