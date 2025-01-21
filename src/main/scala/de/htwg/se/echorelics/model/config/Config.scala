package model.config

/** Configuration settings for the EchoRelics game.
  *
  * @param playerSize
  *   Number of players in the game.
  * @param gridSize
  *   Size of the game grid.
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
    playerSize: Int,
    gridSize: Int,
    relicSpawnRate: Int,
    relicAmount: Int,
    echoIncrementer: Int,
    minHealth: Int,
    wallRatio: Int
)
