package utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{IGrid, ITile}
import model.entity.{IEntity, entityImpl}
import model.item.{ICard, itemImpl}
import utils.TextRenderer
import model.item.inventoryImpl.Inventory

class TextRendererSpec extends AnyWordSpec with Matchers {

  "TextRenderer" should {

    "render the grid correctly" in {
      val grid = new IGrid {
        val tiles: Vector[Vector[ITile]] = Vector(
          Vector(
            ITile(Some(entityImpl.Player("1"))),
            ITile.emptyTile,
            ITile(Some(entityImpl.Wall()))
          ),
          Vector(
            ITile.emptyTile,
            ITile(Some(entityImpl.Relic())),
            ITile.emptyTile
          ),
          Vector(
            ITile(Some(entityImpl.Echo("e1", "1"))),
            ITile.emptyTile,
            ITile.emptyTile
          )
        )
        def set(position: Position, tile: ITile): IGrid = this
        def size: Int = tiles.size
        def tileAt(position: Position): ITile = tiles(position.y)(position.x)
        def isOutOfBounds(position: Position): Boolean = false
        def movePlayer(player: IEntity, direction: Direction): IGrid = this
        def moveEchoes: IGrid = this
        def spawnEcho(position: Position, echo: IEntity): IGrid = this
        def findPlayer(player: IEntity): Option[Position] = None
        def increaseSize: IGrid = this
        def decreaseSize: IGrid = this
        def swap(pos1: Position, pos2: Position): IGrid = this
      }

      val renderedGrid = TextRenderer.render(grid)
      renderedGrid should include("+")
    }

    "render the size prompt correctly" in {
      val renderedPrompt = TextRenderer.renderSizePrompt(10, "Grid")
      renderedPrompt should include("U")
    }

    "render the welcome message correctly" in {
      val renderedMessage = TextRenderer.renderWelcomeMessage
      renderedMessage should include("W")
    }

    "render the input prompt correctly" in {
      val renderedPrompt = TextRenderer.renderInputPrompt
      renderedPrompt should include("Enter your choice:")
    }

    "render the help prompt correctly" in {
      val renderedPrompt = TextRenderer.renderHelpPrompt
      renderedPrompt should include("Use")
    }

    "render an error message correctly" in {
      val renderedError = TextRenderer.renderError("Test error")
      renderedError should include("Test error")
    }

    "render player stats correctly" in {
      val players = List(
        entityImpl.Player(
          "1",
          Stats(3, 1, 0),
          new Inventory().setCard(itemImpl.HealCard())
        ),
        entityImpl.Player("2", Stats(2, 0, 1), new Inventory()),
        entityImpl.Player(
          "3",
          Stats(1, 0, 0),
          new Inventory().setCard(itemImpl.StrikeCard())
        )
      )

      val renderedStats = TextRenderer.renderStats(players)
      renderedStats should include("P")
    }

    "render the current player prompt correctly" in {
      val player = entityImpl.Player("1")
      val renderedPrompt = TextRenderer.renderCurrentPlayerPrompt(player)
      renderedPrompt should include("C")
    }

    "render a card played message correctly" in {
      val player = entityImpl.Player("1")
      val card = itemImpl.HealCard()
      val renderedMessage = TextRenderer.renderCardPlayed(player, card)
      renderedMessage should include("P")
    }

    "clear the screen correctly" in {
      val clearScreen = TextRenderer.clear
      clearScreen should include("\u001b[H\u001b[2J")
    }

    "render a winner message correctly" in {
      val renderedMessage = TextRenderer.renderWinner("1")
      renderedMessage should include("Player 1 won!")
    }
  }
}
