package utils

import scala.io.AnsiColor._
import model.{Grid, Tile}
import model.entity.{Echo, Player, Relic, Wall}
import model.item.{Card, HealCard}

object TextRenderer {
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

  def renderSizePrompt(size: Int, promptType: String): String = {
    val help =
      s"${GREEN}Use ${RED}W${GREEN} and ${RED}S${GREEN} to change the $promptType size${RED}"
    val border = "=" * 24
    val prompt = s"++++++ ${GREEN}$promptType size${RED} : $size ++++++++"
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

  // should render all players stats horizontally
  /* Example:
    Player 1:             Player 2:             Player 3:
      - Health: 3           - Health: 2            - Health: 1
      - Score: 1          - Score: 0             - Score: 0
      - Echoes: 0        - Echoes: 1            - Echoes: 0
      Cards 1: [Heal]     Cards 1: [Empty]      Cards 1: [Empty]
      Cards 2: [Damage]  Cards 2: [Empty]      Cards 2: [Empty]
      Cards 3: [Empty]  Cards 3: [Empty]      Cards 3: [Empty]
   */
  def renderStats(players: List[Player]): String = {
    val spacing = 20
    val headers = players.zipWithIndex
      .map { case (_, index) => s"${CYAN}Player ${index + 1}:${RESET}" }
      .mkString(" " * spacing)
    val healths = players
      .map(player => s"${GREEN}- Health: ${player.stats.health}${RESET}")
      .mkString(" " * spacing)
    val scores = players
      .map(player => s"${YELLOW}- Score: ${player.stats.score}${RESET}")
      .mkString(" " * spacing)
    val echoes = players
      .map(player => s"${RED}- Echoes: ${player.stats.echoes}${RESET}")
      .mkString(" " * spacing)

    val cards = (0 until 3)
      .map { i =>
        players
          .map { player =>
            player.inventory.cardAt(i) match {
              case Some(card) =>
                s"${MAGENTA}Cards ${i + 1}: [${card.name}]${RESET}"
              case None => s"${MAGENTA}Cards ${i + 1}: [Empty]${RESET}"
            }
          }
          .mkString(" " * spacing)
      }
      .mkString("\n")

    s"$headers\n$healths\n$scores\n$echoes\n$cards"
  }

  def renderCurrentPlayerPrompt(player: Player): String = {
    s"${GREEN}Current Player: ${BLUE}${player.id}${RESET}"
  }

  def renderCardPlayed(player: Player, card: Card): String = {
    s"${GREEN}Player ${player.id} played ${RED}${card.name}${RESET}"
  }

  def clear: String = "\u001b[H\u001b[2J"

  private def horizontalLine(size: Int): String = {
    val line = s"${GREEN}+---" * size + "+"
    line + RESET
  }

  def renderTile(tile: Tile): String = {
    val content = tile.entity match {
      case Some(Player(id, _, _)) => s" ${BLUE}${id} "
      case Some(Wall())           => s" ▓ "
      case Some(Relic())          => s" ${MAGENTA}$$ "
      case Some(Echo(id, owner))  => s" ${RED}e "
      case _                      => "   "
    }
    content + RESET
  }
}
