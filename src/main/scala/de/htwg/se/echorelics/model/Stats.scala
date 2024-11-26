package model

case class Stats(
    val relics: Int = 0,
    val echoes: Int = 0,
    val health: Int = 3
) {
  def updateRelics(amount: Int): Stats = this.copy(relics = relics + amount)
  def updateEchoes(amount: Int): Stats = this.copy(echoes = echoes + amount)
  def updateHealth(amount: Int): Stats = this.copy(health = health + amount)
}

object Stats {
  def withHealth(health: Int): Stats = Stats(0, 0, health)
}
