package model

case class Stats(
    val relics: Int = 0,
    val echoes: Int = 0,
    val health: Int = 3
) {
  def addRelic: Stats = {
    new Stats(relics + 1, echoes, health)
  }

  def addEcho: Stats = {
    new Stats(relics, echoes + 1, health)
  }

  def removeEcho: Stats = {
    new Stats(relics, echoes - 1, health)
  }

  def addHealth: Stats = {
    new Stats(relics, echoes, health + 1)
  }

  def removeHealth: Stats = {
    new Stats(relics, echoes, health - 1)
  }
}
