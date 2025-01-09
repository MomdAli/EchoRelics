package utils

case class Stats(
    val score: Int = 0,
    val echoes: Int = 0,
    val health: Int = 3
) {
  def updateScore(amount: Int): Stats = this.copy(score = score + amount)
  def updateEchoes(amount: Int): Stats = this.copy(echoes = echoes + amount)
  def updateHealth(amount: Int): Stats = this.copy(health = health + amount)

  override def toString(): String = {
    s"Score: $score\nEchoes: $echoes\nHealth: $health"
  }
}
