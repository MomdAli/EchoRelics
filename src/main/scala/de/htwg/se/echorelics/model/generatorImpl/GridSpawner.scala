package model.generatorImpl

import com.google.inject.{Guice, Inject}
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import model.{IGrid, ITile}
import model.config.Config
import model.entity.IEntity
import model.IGridSpawner
import modules.EchorelicsModule
import utils.{Position, Direction, Random}

// GridSpawner Facade Design Pattern
class GridSpawner(config: Config) extends IGridSpawner {

  val injector = Guice.createInjector(new EchorelicsModule)

  override def spawnRelic(grid: IGrid): IGrid = {
    val seed = System.currentTimeMillis()
    val gridWithRelics =
      spawnRelics(
        grid,
        seed,
        config.relicAmount
      )
    gridWithRelics
  }

  override def setupStartingGrid(grid: IGrid, players: Seq[IEntity]): IGrid = {
    val seed = System.currentTimeMillis()
    val gridWithWalls = generateWalls(grid, seed, config.wallRatio)
    val gridWithoutCornerWalls = removeCornerWalls(gridWithWalls)
    val gridWithRelics = spawnRelics(
      gridWithoutCornerWalls,
      seed,
      config.relicAmount
    )
    placePlayers(gridWithRelics, players)
  }

  private def generateWalls(grid: IGrid, seed: Long, ratio: Int): IGrid = {
    val numberOfWalls = (grid.size * grid.size) / ratio
    val potentialWalls =
      generatePotentialWalls(grid, seed, numberOfWalls, Set.empty)
    val newGrid = placeValidWalls(grid, potentialWalls, Set.empty)
    newGrid
  }

  private def removeCornerWalls(grid: IGrid): IGrid = {
    val corners = List(
      (Position(0, 0), (1, 1)), // Top-left: add positive offsets
      (Position(0, grid.size - 1), (1, -1)), // Bottom-left: add right and up
      (Position(grid.size - 1, 0), (-1, 1)), // Top-right: add left and down
      (
        Position(grid.size - 1, grid.size - 1),
        (-1, -1)
      ) // Bottom-right: add negative offsets
    )

    val safeZones = corners.flatMap { case (corner, (xOffset, yOffset)) =>
      Set(
        corner, // The corner itself
        Position(corner.x + xOffset, corner.y), // Horizontal adjacent
        Position(corner.x, corner.y + yOffset), // Vertical adjacent
        Position(corner.x + xOffset, corner.y + yOffset) // Diagonal adjacent
      ).filter(pos => pos.isWithinBounds(grid.size, grid.size))
    }.toSet

    replaceTiles(grid, safeZones, ITile.emptyTile)
  }

  private def spawnRelics(
      grid: IGrid,
      seed: Long,
      numberOfRelics: Int
  ): IGrid = {
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

    replaceTiles(
      grid,
      relicPositions,
      ITile(Some(injector.instance[IEntity](Names.named("Relic"))))
    )
  }

  private def placePlayers(grid: IGrid, players: Seq[IEntity]): IGrid = {
    val corners = Seq(
      Position(0, 0),
      Position(0, grid.size - 1),
      Position(grid.size - 1, 0),
      Position(grid.size - 1, grid.size - 1)
    )
    val playerPositions = players.zip(corners)
    playerPositions.foldLeft(grid) { case (currentGrid, (player, position)) =>
      currentGrid.set(position, ITile.spawnEntity(player))
    }
  }

  private def replaceTiles(
      grid: IGrid,
      positions: Set[Position],
      tile: ITile
  ): IGrid = {
    positions.foldLeft(grid) { (currentGrid, position) =>
      currentGrid.set(position, tile)
    }
  }

  private def generatePotentialWalls(
      grid: IGrid,
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
      grid: IGrid,
      potentialWalls: Set[Position],
      processedWalls: Set[Position]
  ): IGrid = {
    if (potentialWalls.isEmpty) grid
    else {
      val position = potentialWalls.head
      val testGrid = grid.set(
        position,
        ITile.spawnEntity(injector.instance[IEntity](Names.named("Wall")))
      )
      val newGrid = if (isGridFullyAccessible(testGrid)) testGrid else grid
      placeValidWalls(newGrid, potentialWalls.tail, processedWalls + position)
    }
  }

  private def isGridFullyAccessible(grid: IGrid): Boolean = {
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
