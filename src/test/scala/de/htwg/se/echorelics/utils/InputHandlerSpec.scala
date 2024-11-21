package utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class InputHandlerSpec extends AnyWordSpec with Matchers {
    "An InputHandler" should {
        "parse 'w' as Direction.Up" in {
            InputHandler.parseInput("w") should be(Some(Direction.Up))
        }

        "parse 's' as Direction.Down" in {
            InputHandler.parseInput("s") should be(Some(Direction.Down))
        }

        "parse 'a' as Direction.Left" in {
            InputHandler.parseInput("a") should be(Some(Direction.Left))
        }

        "parse 'd' as Direction.Right" in {
            InputHandler.parseInput("d") should be(Some(Direction.Right))
        }

        "return None for invalid input" in {
            InputHandler.parseInput("x") should be(None)
        }

        "return None for empty input" in {
            InputHandler.parseInput("") should be(None)
        }

        "return None for mixed case input" in {
            InputHandler.parseInput("W") should be(Some(Direction.Up))
            InputHandler.parseInput("S") should be(Some(Direction.Down))
            InputHandler.parseInput("A") should be(Some(Direction.Left))
            InputHandler.parseInput("D") should be(Some(Direction.Right))
        }
    }
}