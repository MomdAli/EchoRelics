package model.config

// Configurator with Builder design pattern
case class Configurator private (
    playerSize: Int = 2,
    gridSize: Int = 10,
    relicSpawnRate: Int = 15,
    echoIncrementer: Int = 3,
    minHealth: Int = 3,
    wallRatio: Int = 3
) {
  def withPlayer(playerSize: Int): Configurator = {
    playerSize match {
      case 3 =>
        copy(playerSize = playerSize, echoIncrementer = 2, relicSpawnRate = 20)
      case 4 =>
        copy(playerSize = playerSize, echoIncrementer = 1, relicSpawnRate = 25)
      case _ =>
        copy(playerSize = playerSize, echoIncrementer = 3, relicSpawnRate = 15)
    }
  }

  def withGrid(gridSize: Int): Configurator = {
    val wallRatio = (-0.4 * gridSize + 11).toInt
    copy(gridSize = gridSize, wallRatio = wallRatio)
  }

  def build: Config = Config(
    playerSize,
    gridSize,
    relicSpawnRate,
    echoIncrementer,
    minHealth,
    wallRatio
  )
}

object Configurator {
  def apply(): Configurator = new Configurator()
}
