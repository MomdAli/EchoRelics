package model.config

/** Configuration settings for the EchoRelics game.
  *
  * @param playerCount
  *   Number of players in the game.
  * @param gridSize
  *   Size of the game grid.
  * @param maxMoves
  *   Maximum number of moves allowed in the game.
  * @param relicSpawnRate
  *   Rate at which relics spawn on the grid.
  * @param echoIncremeneter
  *   Increment value for echoes.
  * @param minHealth
  *   Minimum health value for players.
  * @param wallRatio
  *   Ratio of walls to other elements on the grid. 2 means 50% of the grid will
  *   be walls.
  */
case class Config(
    playerCount: Int = 2,
    gridSize: Int = 10,
    maxMoves: Int = 100,
    relicSpawnRate: Int = 15,
    echoIncrementer: Int = 3,
    minHealth: Int = 3,
    wallRatio: Int = 3
) {
  require(isValidConfig, "Invalid configuration settings")

  private def isValidConfig: Boolean =
    playerCount >= 2 && playerCount <= 4 &&
      gridSize >= 10 &&
      maxMoves >= 0 &&
      relicSpawnRate >= 15 &&
      echoIncrementer >= 3 &&
      minHealth > 0
    wallRatio >= 2

  def withPlayerCount(playerCount: Int): Config =
    copy(playerCount = playerCount)
  def withGridSize(gridSize: Int): Config = copy(gridSize = gridSize)
  def withMaxMoves(maxMoves: Int): Config = copy(maxMoves = maxMoves)
  def withRelicSpawnRate(relicSpawnRate: Int): Config =
    copy(relicSpawnRate = relicSpawnRate)
  def withEchoIncrementer(echoIncrementer: Int): Config =
    copy(echoIncrementer = echoIncrementer)
  def withMinHealth(minHealth: Int): Config = copy(minHealth = minHealth)
  def withWallRatio(wallRatio: Int): Config = copy(wallRatio = wallRatio)

}

object Config {
  val default: Config = Config()

  // Creates a configuration depending on the number of players and grid size.
  def configurator(
      playerCount: Int,
      gridSize: Int,
      maxMoves: Int = 100
  ): Config = {

    val relicSpawnRate = playerCount match {
      case 2 => 25
      case 3 => 20
      case 4 => 15
      case _ => 15
    }

    val echoIncrementer = playerCount match {
      case 2 => 3
      case 3 => 2
      case 4 => 1
      case _ => 3
    }
    val minHealth = 3
    // The bigger the grid, the less walls there are.
    val wallRatio = (-0.4 * gridSize + 11).toInt

    Config(
      playerCount,
      gridSize,
      maxMoves,
      relicSpawnRate,
      echoIncrementer,
      minHealth,
      wallRatio
    )
  }
}
