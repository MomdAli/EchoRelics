package model.generator

import model.{Grid, Tile, TileContent}
import utils.{Position, Direction}
import model.config.Config

object Spawner {
  def generateWalls(grid: Grid, seed: Long, ratio: Int): Grid = {
    val numberOfWalls =
      // Adjust to control the amount of walls (e.g., 10% of the grid)
      (grid.size * grid.size) / ratio
    val potentialWalls =
      generatePotentialWalls(grid, seed, numberOfWalls, Set.empty)
    val newGrid = placeValidWalls(grid, potentialWalls, Set.empty)
    newGrid
  }

  // expects a grid with walls and players so it shouldn't
  // remove any other content. It should only replace empty tiles
  def spawnRelics(grid: Grid, seed: Long, numberOfRelics: Int): Grid = {
    val emptyPositions = for {
      y <- 0 until grid.size
      x <- 0 until grid.size
      if grid.tileAt(Position(x, y)).content == TileContent.Empty
    } yield Position(x, y)

    val relicsToPlace =
      if (numberOfRelics > emptyPositions.size) emptyPositions.size
      else numberOfRelics

    val random = Random(seed.toInt)
    val (relicPositions, _) = (0 until relicsToPlace).foldLeft(
      (Set.empty[Position], random)
    ) { case ((relics, random), _) =>
      val (index, newRandom) = random.nextInt(emptyPositions.size)
      val position = emptyPositions(index)
      (relics + position, newRandom)
    }

    replaceEmptyTiles(grid, relicPositions, Tile(TileContent.Relic))
  }

  private def replaceEmptyTiles(
      grid: Grid,
      positions: Set[Position],
      tile: Tile
  ): Grid = {
    grid.copy(
      tiles = grid.tiles.zipWithIndex.map { case (row, y) =>
        row.zipWithIndex.map { case (currentTile, x) =>
          val position = Position(x, y)
          if (
            positions
              .contains(position) && currentTile.content == TileContent.Empty
          )
            tile
          else currentTile
        }
      }
    )
  }

  private def generatePotentialWalls(
      grid: Grid,
      seed: Long,
      wallsLeft: Int,
      potentialWalls: Set[Position]
  ): Set[Position] = {
    if (wallsLeft == 0) potentialWalls
    else {
      val random = Random(seed.toInt)
      val (position, newRandom) = random.randomPosition(grid.size)
      if (grid.tileAt(position).content == TileContent.Empty) {
        generatePotentialWalls(
          grid,
          newRandom.seed.toLong,
          wallsLeft - 1,
          potentialWalls + position
        )
      } else {
        generatePotentialWalls(
          grid,
          newRandom.seed.toLong,
          wallsLeft,
          potentialWalls
        )
      }
    }
  }

  private def placeValidWalls(
      grid: Grid,
      potentialWalls: Set[Position],
      processedWalls: Set[Position]
  ): Grid = {
    if (potentialWalls.isEmpty) grid
    else {
      val position = potentialWalls.head
      val testGrid = grid.set(position, Tile(TileContent.Wall))
      val newGrid = if (isGridFullyAccessible(testGrid)) testGrid else grid
      placeValidWalls(newGrid, potentialWalls.tail, processedWalls + position)
    }
  }

  private def isGridFullyAccessible(grid: Grid): Boolean = {
    val start = Position(0, 0)
    if (grid.tileAt(start).content == TileContent.Wall) return false

    def bfs(queue: List[Position], visited: Set[Position]): Set[Position] = {
      queue match {
        case Nil => visited
        case current :: rest =>
          if (
            visited.contains(current) || grid
              .tileAt(current)
              .content == TileContent.Wall
          ) {
            bfs(rest, visited)
          } else {
            val newVisited = visited + current
            val allNeighbors =
              neighbors(current, grid.size).filterNot(visited.contains)
            bfs(rest ++ allNeighbors, newVisited)
          }
      }
    }

    val visited = bfs(List(Position(0, 0)), Set.empty)
    visited.size == grid.size * grid.size - grid.tiles.flatten.count(
      _.content == TileContent.Wall
    )
  }

  private def neighbors(position: Position, size: Int): Seq[Position] = {
    val directions =
      Seq(Position(0, 1), Position(1, 0), Position(0, -1), Position(-1, 0))
    directions
      .map(dir => position + dir)
      .filter(pos => pos.isWithinBounds(size, size))
  }
}
