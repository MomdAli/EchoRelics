package view.tui

import scala.io.AnsiColor._

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Tile, Stats}
import model.entity.{Player, Relic, Wall, Echo}
import utils.Position
import model.item.{Card, HealCard}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar

class TextRendererSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A TextRenderer" should {

    "render a grid correctly" in {
      val gridSize = 3
      val grid = mock[Grid]
      when(grid.size).thenReturn(gridSize)
      when(grid.tileAt(Position(0, 0))).thenReturn(Tile(Some(Player("1"))))
      when(grid.tileAt(Position(1, 0))).thenReturn(Tile(Some(Wall())))
      when(grid.tileAt(Position(2, 0))).thenReturn(Tile(Some(Relic())))
      when(grid.tileAt(Position(0, 1)))
        .thenReturn(Tile(Some(Echo("1", Player("1")))))
      when(grid.tileAt(Position(1, 1))).thenReturn(Tile(None))
      when(grid.tileAt(Position(2, 1))).thenReturn(Tile(Some(Player("2"))))
      when(grid.tileAt(Position(0, 2))).thenReturn(Tile(Some(Wall())))
      when(grid.tileAt(Position(1, 2))).thenReturn(Tile(Some(Relic())))
      when(grid.tileAt(Position(2, 2)))
        .thenReturn(Tile(Some(Echo("2", Player("2")))))

      val renderedGrid = TextRenderer.render(grid)
      val expectedGrid =
        s"""${GREEN}+---+---+---+${RESET}
                 |${GREEN}|${RESET} ${BLUE}1 ${RESET}${GREEN}|${RESET} ▓ ${RESET}${GREEN}|${RESET} ${MAGENTA}$$ ${RESET}${GREEN}|${RESET}
                 |${GREEN}+---+---+---+${RESET}
                 |${GREEN}|${RESET} ${RED}a ${RESET}${GREEN}|${RESET}   ${RESET}${GREEN}|${RESET} ${BLUE}2 ${RESET}${GREEN}|${RESET}
                 |${GREEN}+---+---+---+${RESET}
                 |${GREEN}|${RESET} ▓ ${RESET}${GREEN}|${RESET} ${MAGENTA}$$ ${RESET}${GREEN}|${RESET} ${RED}e ${RESET}${GREEN}|${RESET}
                 |${GREEN}+---+---+---+${RESET}""".stripMargin

      renderedGrid should not be (expectedGrid)
    }

    "render a size prompt correctly" in {
      val size = 10
      val promptType = "Grid"
      val renderedPrompt = TextRenderer.renderSizePrompt(size, promptType)
      renderedPrompt should not be (
        s"${GREEN}Use ${RED}W${GREEN} and ${RED}S${GREEN} to change the Grid size${RED}\n" +
          "=" * 24 + "\n" +
          s"++++++ ${GREEN}Play size${RED} : 10 ++++++++\n" +
          "=" * 24 + s"${RESET}"
      )
    }

    "render a welcome message correctly" in {
      val welcomeMessage = TextRenderer.renderWelcomeMessage
      welcomeMessage should be(
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
      )
    }

    "render an input prompt correctly" in {
      val inputPrompt = TextRenderer.renderInputPrompt
      inputPrompt should be(s"${GREEN}Enter your choice: ${RESET}".stripMargin)
    }

    "render a help prompt correctly" in {
      val helpPrompt = TextRenderer.renderHelpPrompt
      helpPrompt should be(
        s"${GREEN}Use ${RED}W${GREEN}, ${RED}A${GREEN}, ${RED}S${GREEN}, ${RED}D${GREEN} to move${RED}\n" +
          s"${GREEN}and ${RED}E${GREEN} to spawn an echo${RESET}".stripMargin
      )
    }

    "render an error message correctly" in {
      val errorMessage = "An error occurred"
      val renderedError = TextRenderer.renderError(errorMessage)
      renderedError should be(s"${RED}An error occurred${RESET}".stripMargin)
    }

    "render player stats correctly" in {
      val player1 = Player("1", mock[Stats])
      val player2 = Player("2", mock[Stats])
      val players = List(player1, player2)
      val renderedStats = TextRenderer.renderStats(players)
      renderedStats should include("Player 1:")
      renderedStats should include("Player 2:")
    }

    "render current player prompt correctly" in {
      val player = Player("1", mock[Stats])
      val currentPlayerPrompt = TextRenderer.renderCurrentPlayerPrompt(player)
      currentPlayerPrompt should be(
        s"${GREEN}Current Player: ${BLUE}1${RESET}".stripMargin
      )
    }

    "render card played message correctly" in {
      val player = Player("1", mock[Stats])
      val card = HealCard()
      val cardPlayedMessage = TextRenderer.renderCardPlayed(player, card)
      cardPlayedMessage should be(
        s"${GREEN}Player 1 played ${RED}Heal${RESET}".stripMargin
      )
    }

    "clear the screen correctly" in {
      val clearScreen = TextRenderer.clear
      clearScreen should be("\u001b[H\u001b[2J".stripMargin)
    }

    "render a tile correctly" in {
      val player = Player("1")
      val playerTile = Tile(Some(player))
      val wallTile = Tile(Some(Wall()))
      val relicTile = Tile(Some(Relic()))
      val echoTile = Tile(Some(Echo("1", player)))
      val emptyTile = Tile(None)

      TextRenderer.renderTile(playerTile) should be(
        s" ${BLUE}1 ${RESET}".stripMargin
      )
      TextRenderer.renderTile(wallTile) should be(s" ▓ ${RESET}".stripMargin)
      TextRenderer.renderTile(relicTile) should be(
        s" ${MAGENTA}$$ ${RESET}".stripMargin
      )
      TextRenderer.renderTile(echoTile) should be(
        s" ${RED}e ${RESET}".stripMargin
      )
      TextRenderer.renderTile(emptyTile) should not be ("   ".stripMargin)
    }
  }
}
