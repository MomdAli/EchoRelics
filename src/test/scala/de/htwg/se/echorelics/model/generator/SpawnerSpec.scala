package model.generator

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Tile, TileContent}
import utils.Position

class SpawnerSpec extends AnyWordSpec with Matchers {

  "A Spawner" should {

    "generate walls correctly" in {
      val grid = new Grid(10)
      val newGrid = Spawner.generateWalls(grid, 12345L, 10)
      val wallCount = newGrid.tiles.flatten.count(_.content == TileContent.Wall)
      wallCount should be > 0
    }

    "spawn relics correctly" in {
      val grid = new Grid(10)
      val gridWithWalls = Spawner.generateWalls(grid, 12345L, 10)
      val newGrid = Spawner.spawnRelics(gridWithWalls, 12345L, 5)
      val relicCount =
        newGrid.tiles.flatten.count(_.content == TileContent.Relic)
      relicCount should be(5)
    }

    "not place relics on non-empty tiles" in {
      val grid = new Grid(10).set(Position(0, 0), Tile(TileContent.Wall))
      val newGrid = Spawner.spawnRelics(grid, 12345L, 1)
      newGrid.tileAt(Position(0, 0)).content should be(TileContent.Wall)
    }

    "ensure grid is fully accessible after placing walls" in {
      val grid = new Grid(10)
      val newGrid = Spawner.generateWalls(grid, 12345L, 10)
      val isAccessible = {
        val method = classOf[Spawner.type]
          .getDeclaredMethod("isGridFullyAccessible", classOf[Grid])
        method.setAccessible(true)
        method.invoke(Spawner, newGrid).asInstanceOf[Boolean]
      }
      isAccessible should be(true)
    }

    "not place walls on non-empty tiles" in {
      val grid = new Grid(10).set(Position(0, 0), Tile(TileContent.Relic))
      val newGrid = Spawner.generateWalls(grid, 12345L, 10)
      newGrid.tileAt(Position(0, 0)).content should be(TileContent.Relic)
    }
  }
}
