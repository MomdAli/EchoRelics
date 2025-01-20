package model.gridImpl

import com.google.inject.Inject

import model.{IGrid, ITile}
import model.entity.IEntity
import model.events.{EventManager, GameEvent}
import utils.{Direction, Position, Random}

case class Grid @Inject() (tiles: Vector[Vector[ITile]] = Vector.empty)
    extends IGrid {

  def this(size: Int) = this(Vector.fill(size, size)(ITile.emptyTile))

  override def set(position: Position, tile: ITile): Grid = {
    copy(tiles.updated(position.y, tiles(position.y).updated(position.x, tile)))
  }

  override def size: Int = tiles.size

  override def tileAt(position: Position): ITile = {
    if (isOutOfBounds(position)) {
      ITile.emptyTile
    } else {
      tiles(position.y)(position.x)
    }
  }

  override def isOutOfBounds(position: Position): Boolean = {
    position.x < 0 || position.y < 0 || position.x >= size || position.y >= size
  }

  override def movePlayer(
      player: IEntity,
      direction: Direction
  ): Grid = {
    findPlayer(player) match {
      case Some(playerPosition) =>
        val newPosition = playerPosition.move(direction)
        if (
          isOutOfBounds(newPosition) || (!tileAt(
            newPosition
          ).isWalkable && !tileAt(newPosition).entity
            .exists(e => IEntity.isEcho(e) && e.owner == player.id))
        ) {
          this
        } else {
          val newGrid = set(playerPosition, ITile.emptyTile)
          checkCollect(player, newPosition)
          newGrid.set(newPosition, ITile(Some(player)))
        }
      case None => this
    }
  }

  private def checkCollect(player: IEntity, position: Position) = {
    if (tileAt(position).entity.exists(_.isCollectable)) {
      val relic = tileAt(position).entity.get
      EventManager.notify(
        GameEvent.OnRelicCollectEvent(player, relic)
      )
    }
  }

  override def moveEchoes: IGrid = {
    val echoPositions = for {
      y <- 0 until size
      x <- 0 until size
      if tileAt(Position(x, y)).entity.exists(IEntity.isEcho)
    } yield Position(x, y)

    echoPositions.foldLeft(this) { (currentGrid, position) =>
      val echo = currentGrid.tileAt(position).entity.get
      val rng = Random(System.currentTimeMillis().toInt)
      val (shuffledDirections, _) = rng.shuffle(Direction.values.toList)

      def tryMoveEcho(
          directions: List[Direction],
          grid: Grid
      ): (Grid, Position) = {
        directions match {
          case Nil =>
            (grid, position) // No move possible, return original position
          case direction :: rest =>
            val newPosition = position.move(direction)
            if (
              !grid.isOutOfBounds(newPosition) &&
              (grid.tileAt(newPosition).isEmpty || grid
                .tileAt(newPosition)
                .hasRelic)
            ) {

              // Check if there's a relic to collect
              if (grid.tileAt(newPosition).hasRelic) {
                val relic = grid.tileAt(newPosition).entity.get
                EventManager.notify(
                  GameEvent.OnRelicCollectEvent(
                    grid.getPlayerByID(echo.owner).get, // Get the owner player
                    relic
                  )
                )
              }

              (
                grid
                  .set(position, ITile.emptyTile)
                  .set(newPosition, ITile(Some(echo))),
                newPosition
              )
            } else {
              tryMoveEcho(rest, grid)
            }
        }
      }

      def checkAndHandlePlayerNearby(
          grid: Grid,
          echoPosition: Position
      ): Grid = {
        // Get all cardinal positions
        val cardinalPositions = Direction.values.flatMap { direction =>
          val newPos = echoPosition.move(direction)
          if (!grid.isOutOfBounds(newPos)) Some(newPos) else None
        }

        // Create diagonal positions by combining cardinal directions
        val diagonalPositions = for {
          vertical <- List(Position.Up, Position.Down)
          horizontal <- List(Position.Left, Position.Right)
          newPos = echoPosition + vertical + horizontal
          if !grid.isOutOfBounds(newPos)
        } yield newPos

        // Combine all positions
        val allNearbyPositions = cardinalPositions ++ diagonalPositions

        val playerNearby = allNearbyPositions
          .map(grid.tileAt)
          .flatMap(_.entity)
          .filter(entity => IEntity.isPlayer(entity) && entity.id != echo.owner)
          .headOption

        playerNearby match {
          case Some(player) =>
            EventManager.notify(GameEvent.OnPlayerDamageEvent(player))
            grid.set(echoPosition, ITile.emptyTile)
          case None =>
            grid
        }
      }

      // Try to move the echo and then check for nearby players
      val (movedGrid, finalPosition) =
        tryMoveEcho(shuffledDirections, currentGrid)
      checkAndHandlePlayerNearby(movedGrid, finalPosition)
    }
  }

  private def getPlayerByID(id: String): Option[IEntity] = {
    for {
      y <- 0 until size
      x <- 0 until size
      position = Position(x, y)
      entity <- tileAt(position).entity
      if IEntity.isPlayer(entity) && entity.id == id
    } yield entity
  }.headOption

  override def spawnEcho(position: Position, echo: IEntity): Grid = {
    val rng = Random(System.currentTimeMillis().toInt)
    val directions = Direction.values.toList
    val (shuffledDirections, _) = rng.shuffle(directions)

    def trySpawn(directions: List[Direction], grid: Grid): Grid = {
      directions match {
        case Nil =>
          EventManager.notify(GameEvent.OnErrorEvent("Failed to spawn echo"))
          grid
        case direction :: rest =>
          val newPosition = position.move(direction)
          if (!isOutOfBounds(newPosition) && tileAt(newPosition).isEmpty) {
            grid.set(newPosition, ITile(Some(echo)))
          } else {
            trySpawn(rest, grid)
          }
      }
    }

    trySpawn(shuffledDirections, this)
  }

  override def findPlayer(player: IEntity): Option[Position] = {
    val position = for {
      y <- 0 until size
      x <- 0 until size
      if tileAt(Position(x, y)).isPlayer(player)
    } yield Position(x, y)
    position.headOption
  }

  override def increaseSize: Grid = {
    if (size >= 15) {
      this
    } else
      new Grid(size + 1)
  }

  override def decreaseSize: Grid = {
    if (size <= 10) {
      this
    } else
      new Grid(size - 1)
  }

  override def swap(pos1: Position, pos2: Position): Grid = {
    val tile1 = tileAt(pos1)
    val tile2 = tileAt(pos2)
    set(pos1, tile2).set(pos2, tile1)
  }
}
