package view

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.{Grid, Tile, TileContent}
import utils.Position

class DisplayRendererSpec extends AnyWordSpec with Matchers {

  "A DisplayRenderer" should {

    "render grid size prompt correctly" in {
      val size = 10
      val rendered = DisplayRenderer.renderSizePrompt(size, "Grid")
      rendered.should(include("Grid size"))
      rendered.should(include(s"$size"))
    }

    "render player size prompt correctly" in {
      val size = 2
      val rendered = DisplayRenderer.renderSizePrompt(size, "Player")
      rendered.should(include("Player size"))
      rendered.should(include(s"$size"))
    }

    "render welcome message correctly" in {
      val rendered = DisplayRenderer.renderWelcomeMessage
      rendered should include("Welcome to EchoRelics!")
      rendered should include(
        "Your goal is to collect all relics while avoiding echoes of your past moves."
      )
    }

    "render input prompt correctly" in {
      val rendered = DisplayRenderer.renderInputPrompt
      rendered should include("Enter your choice:")
    }

    "render error message correctly" in {
      val message = "An error occurred"
      val rendered = DisplayRenderer.renderError(message)
      rendered should include(message)
    }

    "clear the screen correctly" in {
      val rendered = DisplayRenderer.clear
      rendered should be("\u001b[H\u001b[2J")
    }

    "render help prompt correctly" in {
      val rendered = DisplayRenderer.renderHelpPrompt
      rendered should include("Use")
      rendered should include("to move")
      rendered should include("and")
      rendered should include("to spawn an echo")
    }

    "render a grid correctly" in {
      val grid = new Grid(3)
      val rendered = DisplayRenderer.render(grid)
      rendered should include("|")
    }
  }
}
