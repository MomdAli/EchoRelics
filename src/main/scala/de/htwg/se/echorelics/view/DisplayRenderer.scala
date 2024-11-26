package view

import scala.io.AnsiColor._
import model.{Grid, Tile, TileContent}
import utils.Position

object DisplayRenderer {
  def render(grid: Grid): String = {
    val horizontal = horizontalLine(grid.size)

    val rows = for (y <- 0 until grid.size) yield {
      val row = for (x <- 0 until grid.size) yield {
        val tile = grid.tileAt(Position(x, y))
        s"${GREEN}|${RESET}${renderTile(tile)}"
      }
      row.mkString + s"${GREEN}|${RESET}"
    }

    (horizontal +: rows.flatMap(row => Seq(row, horizontal)))
      .mkString("\n")
  }

  def renderGridSizePrompt(size: Int): String = {
    val help =
      s"${GREEN}Use ${RED}W${GREEN} and ${RED}S${GREEN} to change the grid size${RED}"
    val border = "=" * 24
    val prompt = s"++++++ ${GREEN}Grid size${RED} : $size ++++++++"
    s"${RED}$help\n$border\n$prompt\n$border$RESET"
  }

  def renderPlayerSizePrompt(size: Int): String = {
    val help =
      s"${GREEN}Use ${RED}W${GREEN} and ${RED}S${GREEN} to change the player size${RED}"
    val border = "=" * 24
    val prompt = s"++++++ ${GREEN}Player size${RED} : $size ++++++++"
    s"${RED}$help\n$border\n$prompt\n$border$RESET"
  }

  def renderWelcomeMessage: String = {
    s"""${CYAN}Welcome to EchoRelics!${RESET}
       |${YELLOW}Your goal is to collect all relics while avoiding echoes of your past moves.${RESET}
       |
       |${GREEN}Controls:
       |Set Grid Size: ${RED}G${RESET} (size: 10-20)
       |${GREEN}Set Player Size: ${RED}Z${RESET} (size: 2-4)
       |${GREEN}Start Game: ${RED}N
       |
       |${GREEN}Good luck!
       |Enter your choice: ${RESET}""".stripMargin
  }

  def renderInputPrompt: String = {
    s"${GREEN}Enter your choice: ${RESET}"
  }

  def renderHelpPrompt: String = {
    s"${GREEN}Use ${RED}W${GREEN}, ${RED}A${GREEN}, ${RED}S${GREEN}, ${RED}D${GREEN} to move${RED}\n"
      + s"${GREEN}and ${RED}E${GREEN} to spawn an echo${RESET}"
  }

  def renderError(message: String): String = {
    s"${RED}$message${RESET}"
  }

  def clear: String = "\u001b[H\u001b[2J"

  private def horizontalLine(size: Int): String = {
    val line = s"${GREEN}+---" * size + "+"
    line + RESET
  }

  private def renderTile(tile: Tile): String = {
    val content = tile.content match {
      case TileContent.Empty      => "   "
      case TileContent.Player(id) => s" ${BLUE}${id} "
      case TileContent.Wall       => s" ▓ "
      case TileContent.Relic      => s" ${MAGENTA}$$ "
      case TileContent.Echo       => s" ${RED}e "
      case _                      => "   "
    }
    content + RESET
  }
}
