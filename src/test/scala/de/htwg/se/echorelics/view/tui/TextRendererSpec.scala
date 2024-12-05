package view.tui

import scala.io.AnsiColor._

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.entity.{Echo, Player, Relic, Wall}
import model.{Grid, Tile, Stats}
import model.item.{Card, HealCard, Inventory}
import utils.Position

class TextRendererSpec extends AnyWordSpec with Matchers {

  "TextRenderer" should {

    "render size prompt correctly" in {
      val expectedOutput =
        s"${RED}${GREEN}Use ${RED}W${GREEN} and ${RED}S${GREEN} to change the Grid size${RED}\n========================\n++++++ ${GREEN}Grid size${RED} : 10 ++++++++\n========================${RESET}"
      TextRenderer.renderSizePrompt(10, "Grid") should be(expectedOutput)
    }

    "render welcome message correctly" in {
      val expectedOutput =
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
      TextRenderer.renderWelcomeMessage should be(expectedOutput)
    }

    "render input prompt correctly" in {
      val expectedOutput = s"${GREEN}Enter your choice: ${RESET}"
      TextRenderer.renderInputPrompt should be(expectedOutput)
    }

    "render help prompt correctly" in {
      val expectedOutput =
        s"${GREEN}Use ${RED}W${GREEN}, ${RED}A${GREEN}, ${RED}S${GREEN}, ${RED}D${GREEN} to move${RED}\n${GREEN}and ${RED}E${GREEN} to spawn an echo${RESET}"
      TextRenderer.renderHelpPrompt should be(expectedOutput)
    }

    "render error message correctly" in {
      val expectedOutput = s"${RED}Error occurred${RESET}"
      TextRenderer.renderError("Error occurred") should be(expectedOutput)
    }

    "render current player prompt correctly" in {
      val player = Player("1", Stats(0, 0, 3), null)
      val expectedOutput = s"${GREEN}Current Player: ${BLUE}1${RESET}"
      TextRenderer.renderCurrentPlayerPrompt(player) should be(expectedOutput)
    }

    "render card played message correctly" in {
      val player = Player("1", Stats(0, 0, 3), null)
      val card = HealCard()
      val expectedOutput = s"${GREEN}Player 1 played ${RED}Heal${RESET}"
      TextRenderer.renderCardPlayed(player, card) should be(expectedOutput)
    }

    "clear the screen correctly" in {
      val expectedOutput = "\u001b[H\u001b[2J"
      TextRenderer.clear should be(expectedOutput)
    }
  }
}
