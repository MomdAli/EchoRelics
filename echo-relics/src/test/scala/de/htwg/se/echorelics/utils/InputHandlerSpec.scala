package de.htwg.se.echorelics.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.echorelics.math.Direction

class InputHandlerSpec extends AnyWordSpec with Matchers {
  "An InputHandler" should {
    "parse 'w' as Move Up" in {
      InputHandler.parseInput("w") should be(Some(Input.Move(Direction.Up)))
    }
    "parse 's' as Move Down" in {
      InputHandler.parseInput("s") should be(Some(Input.Move(Direction.Down)))
    }
    "parse 'a' as Move Left" in {
      InputHandler.parseInput("a") should be(Some(Input.Move(Direction.Left)))
    }
    "parse 'd' as Move Right" in {
      InputHandler.parseInput("d") should be(Some(Input.Move(Direction.Right)))
    }
    "parse 'q' as Quit" in {
      InputHandler.parseInput("q") should be(Some(Input.Quit))
    }
    "return None for invalid input" in {
      InputHandler.parseInput("x") should be(None)
    }
    "be case insensitive" in {
      InputHandler.parseInput("W") should be(Some(Input.Move(Direction.Up)))
      InputHandler.parseInput("S") should be(Some(Input.Move(Direction.Down)))
      InputHandler.parseInput("A") should be(Some(Input.Move(Direction.Left)))
      InputHandler.parseInput("D") should be(Some(Input.Move(Direction.Right)))
      InputHandler.parseInput("Q") should be(Some(Input.Quit))
    }
  }
}
