import scala.io.AnsiColor._

import de.htwg.se.echorelics.core.GameManager
import de.htwg.se.echorelics.core.Grid
import de.htwg.se.echorelics.model.Player

@main def echorelics(): Unit = {
  println(s"\n${GREEN}Welcome to Echo Relics${RESET}\n")

  // Setup the grid and the player
  val grid = Grid.defaultGrid()
  val player = Player.defaultPlayer()

  // Initialize the game controller and start the game
  val gameManager = new GameManager(player, grid)
  gameManager.runGame()
}
