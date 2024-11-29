package model.generator

import model.{Grid, Tile}
import utils.{Position, Direction}
import model.config.Config
import model.entity.{Player, Relic, Wall}

// GridSpawner Facade Design Pattern
class GridSpawner(config: Config) {

  def setupStartingGrid(grid: Grid, players: Seq[Player]): Grid = {
    val seed = System.currentTimeMillis()
    val gridWithWalls = generateWalls(grid, seed, config.wallRatio)
    val gridWithoutCornerWalls = removeCornerWalls(gridWithWalls)
    val gridWithRelics = spawnRelics(
      gridWithoutCornerWalls,
      seed,
      3
    ) // ! hardcoded number of relics
    placePlayers(gridWithRelics, players)
  }

  private def generateWalls(grid: Grid, seed: Long, ratio: Int): Grid = {
    val numberOfWalls = (grid.size * grid.size) / ratio
    val potentialWalls =
      generatePotentialWalls(grid, seed, numberOfWalls, Set.empty)
    val newGrid = placeValidWalls(grid, potentialWalls, Set.empty)
    newGrid
  }

  private def removeCornerWalls(grid: Grid): Grid = {
    val corners = Set(
      Position(0, 0),
      Position(0, grid.size - 1),
      Position(grid.size - 1, 0),
      Position(grid.size - 1, grid.size - 1)
    )
    replaceTiles(grid, corners, Tile.EmptyTile)
  }

  private def spawnRelics(grid: Grid, seed: Long, numberOfRelics: Int): Grid = {
    val emptyPositions = for {
      y <- 0 until grid.size
      x <- 0 until grid.size
      if grid.tileAt(Position(x, y)).entity.isEmpty
    } yield Position(x, y)

    val relicsToPlace =
      if (numberOfRelics > emptyPositions.size) emptyPositions.size
      else numberOfRelics

    val random = Random(seed.toInt)
    val (relicPositions, _) =
      (0 until relicsToPlace).foldLeft((Set.empty[Position], random)) {
        case ((relics, random), _) =>
          val (index, newRandom) = random.nextInt(emptyPositions.size)
          val position = emptyPositions(index)
          (relics + position, newRandom)
      }

    replaceTiles(grid, relicPositions, Tile(Some(Relic())))
  }

  private def placePlayers(grid: Grid, players: Seq[Player]): Grid = {
    val corners = Seq(
      Position(0, 0),
      Position(0, grid.size - 1),
      Position(grid.size - 1, 0),
      Position(grid.size - 1, grid.size - 1)
    )
    val playerPositions = players.zip(corners)
    playerPositions.foldLeft(grid) { case (currentGrid, (player, position)) =>
      currentGrid.set(position, Tile.PlayerTile(player))
    }
  }

  private def replaceTiles(
      grid: Grid,
      positions: Set[Position],
      tile: Tile
  ): Grid = {
    grid.copy(
      tiles = grid.tiles.zipWithIndex.map { case (row, y) =>
        row.zipWithIndex.map { case (currentTile, x) =>
          val position = Position(x, y)
          if (positions.contains(position)) tile else currentTile
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
      if (grid.tileAt(position).entity.isEmpty) {
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
      val testGrid = grid.set(position, Tile(Some(Wall())))
      val newGrid = if (isGridFullyAccessible(testGrid)) testGrid else grid
      placeValidWalls(newGrid, potentialWalls.tail, processedWalls + position)
    }
  }

  private def isGridFullyAccessible(grid: Grid): Boolean = {
    val start = Position(0, 0)
    if (grid.tileAt(start).hasWall) return false

    def bfs(queue: List[Position], visited: Set[Position]): Set[Position] = {
      queue match {
        case Nil => visited
        case current :: rest =>
          if (
            visited.contains(current) || grid
              .tileAt(current)
              .hasWall
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
      _.hasWall
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
