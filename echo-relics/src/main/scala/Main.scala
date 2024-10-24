import scala.io.AnsiColor._
import com.echorelics.core.{Grid, GameManager}
import com.echorelics.math.Position
import com.echorelics.model.Player

@main def echorelics(): Unit = {
  println(s"\n${GREEN}Welcome to Echo Relics${RESET}\n")

  // Setup the grid and the player
  val grid = Grid.defaultGrid()
  val player = Player.defaultPlayer(grid)

  // Initialize the game controller and start the game
  val gameManager = new GameManager(player, grid)
  gameManager.runGame()
}
