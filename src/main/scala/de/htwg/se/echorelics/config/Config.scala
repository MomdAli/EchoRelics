package config

/** Configuration settings for the EchoRelics game.
  *
  * @param playerCount
  *   Number of players in the game. Default is 2.
  * @param gridSize
  *   Size of the game grid. Default is 10.
  * @param maxMoves
  *   Maximum number of moves allowed in the game. Default is 100.
  * @param relicSpawnRate
  *   Rate at which relics spawn on the grid. Default is 10.
  * @param echoIncremeneter
  *   Increment value for echoes. Default is 3.
  * @param minHealth
  *   Minimum health value for players. Default is 3.
  * @param wallRatio
  *   Ratio of walls to other elements on the grid. 2 means 50% of the grid will
  *   be walls. Default is 2.
  */
case class Config(
    playerCount: Int = 2,
    gridSize: Int = 10,
    maxMoves: Int = 100,
    relicSpawnRate: Int = 15,
    echoIncrementer: Int = 3,
    minHealth: Int = 3,
    wallRatio: Int = 2
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
    val wallRatio = gridSize match {
      case 10 => 4
      case 15 => 3
      case 20 => 2
      case _  => 4
    }

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
