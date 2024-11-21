package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameStateSpec extends AnyWordSpec with Matchers {
    "A GameState" should {
        "be NotStarted" in {
            GameState.NotStarted should be(GameState.NotStarted)
        }
        "be Running" in {
            GameState.Running should be(GameState.Running)
        }
        "be GameOver" in {
            GameState.GameOver should be(GameState.GameOver)
        }
        "be Paused" in {
            GameState.Paused should be(GameState.Paused)
        }
        "be Victory" in {
            GameState.Victory should be(GameState.Victory)
        }
    }
}