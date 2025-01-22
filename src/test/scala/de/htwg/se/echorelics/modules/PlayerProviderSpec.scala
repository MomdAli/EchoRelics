package modules

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.entity.entityImpl.Player

class PlayerProviderSpec extends AnyWordSpec with Matchers {

  "A PlayerProvider" should {

    "provide a Player with the correct id" in {
      val provider = new PlayerProvider
      provider.setId("test-id")
      val player = provider.get()
      player.id should be("test-id")
    }

    "provide a Player with default stats" in {
      val provider = new PlayerProvider
      provider.setId("test-id")
      val player = provider.get()
      player.stats.health should be(3)
      player.stats.score should be(0)
      player.stats.echoes should be(1)
    }

    "provide a Player with an empty inventory" in {
      val provider = new PlayerProvider
      provider.setId("test-id")
      val player = provider.get()
      player.inventory.isFull should be(false)
    }

    "update the id correctly" in {
      val provider = new PlayerProvider
      provider.setId("initial-id")
      provider.setId("updated-id")
      val player = provider.get()
      player.id should be("updated-id")
    }
  }
}
