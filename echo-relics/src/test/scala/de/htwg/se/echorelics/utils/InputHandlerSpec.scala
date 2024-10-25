package de.htwg.se.echorelics.utils

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.echorelics.math.Direction

class InputHandlerSpec extends AnyWordSpec {

    "InputHandler" should {
        "return Move(Direction.Up) for input 'w'" in {
            InputHandler.handleInput("w") should be (Input.Move(Direction.Up))
        }

        "return Move(Direction.Left) for input 'a'" in {
            InputHandler.handleInput("a") should be (Input.Move(Direction.Left))
        }

        "return Move(Direction.Down) for input 's'" in {
            InputHandler.handleInput("s") should be (Input.Move(Direction.Down))
        }

        "return Move(Direction.Right) for input 'd'" in {
            InputHandler.handleInput("d") should be (Input.Move(Direction.Right))
        }

        "return Quit for input 'q'" in {
            InputHandler.handleInput("q") should be (Input.Quit)
        }

        "return Quit for input 'quit'" in {
            InputHandler.handleInput("quit") should be (Input.Quit)
        }

        "return Quit for input 'stop'" in {
            InputHandler.handleInput("stop") should be (Input.Quit)
        }

        "return Quit for input 'exit'" in {
            InputHandler.handleInput("exit") should be (Input.Quit)
        }

        "throw IllegalArgumentException for invalid input" in {
            an [IllegalArgumentException] should be thrownBy InputHandler.handleInput("invalid")
        }
    }
}
