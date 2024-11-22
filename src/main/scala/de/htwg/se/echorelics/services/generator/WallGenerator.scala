package services.generator

import model.{Grid, Tile, TileContent}
import utils.{Position, Direction}
import config.Config

object WallGenerator {
  def generateWalls(grid: Grid, seed: Long, config: Config): Grid = {
    val numberOfWalls =
      (grid.size * grid.size) / config.wallRatio // Adjust to control the amount of walls (e.g., 10% of the grid)
    val potentialWalls =
      generatePotentialWalls(grid, seed, numberOfWalls, Set.empty)
    val newGrid = placeValidWalls(grid, potentialWalls, Set.empty)
    newGrid
  }

  private def generatePotentialWalls(
      grid: Grid,
      seed: Long,
      wallsLeft: Int,
      potentialWalls: Set[Position]
  ): Set[Position] = {
    if (wallsLeft == 0) potentialWalls
    else {
      val (newSeed, position) = RandomGenerator.randomPosition(seed, grid.size)
      if (grid.tileAt(position).content == TileContent.Empty) {
        generatePotentialWalls(
          grid,
          newSeed,
          wallsLeft - 1,
          potentialWalls + position
        )
      } else {
        generatePotentialWalls(grid, newSeed, wallsLeft, potentialWalls)
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
            val neighbors =
              getNeighbors(current, grid.size).filterNot(visited.contains)
            bfs(rest ++ neighbors, newVisited)
          }
      }
    }

    val visited = bfs(List(Position(0, 0)), Set.empty)
    visited.size == grid.size * grid.size - grid.tiles.flatten.count(
      _.content == TileContent.Wall
    )
  }

  private def getNeighbors(position: Position, size: Int): Seq[Position] = {
    val directions =
      Seq(Position(0, 1), Position(1, 0), Position(0, -1), Position(-1, 0))
    directions
      .map(dir => position + dir)
      .filter(pos => pos.isWithinBounds(size, size))
  }
}
