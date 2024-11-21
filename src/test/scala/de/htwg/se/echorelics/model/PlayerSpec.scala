package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends AnyWordSpec with Matchers {
    "A Player" should {
        "have an id" in {
            val player = Player("123")
            player.id should be("123")
        }

        "be equal to another player with the same id" in {
            val player1 = Player("123")
            val player2 = Player("123")
            player1 should be(player2)
        }

        "not be equal to another player with a different id" in {
            val player1 = Player("123")
            val player2 = Player("456")
            player1 should not be player2
        }
    }
}