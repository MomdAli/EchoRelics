package model.config

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ConfiguratorSpec extends AnyWordSpec with Matchers {

  "A Configurator" should {

    "have default values" in {
      val configurator = Configurator()
      val config = configurator.build

      config.playerSize should be(2)
      config.gridSize should be(10)
      config.relicSpawnRate should be(15)
      config.echoIncrementer should be(3)
      config.minHealth should be(3)
      config.wallRatio should be(3)
    }

    "set player size to 3 and adjust other values accordingly" in {
      val configurator = Configurator().withPlayer(3)
      val config = configurator.build

      config.playerSize should be(3)
      config.echoIncrementer should be(2)
      config.relicSpawnRate should be(20)
    }

    "set player size to 4 and adjust other values accordingly" in {
      val configurator = Configurator().withPlayer(4)
      val config = configurator.build

      config.playerSize should be(4)
      config.echoIncrementer should be(1)
      config.relicSpawnRate should be(25)
    }

    "set grid size and adjust wall ratio accordingly" in {
      val configurator = Configurator().withGrid(20)
      val config = configurator.build

      config.gridSize should be(20)
      config.wallRatio should be(3) // (-0.4 * 20 + 11).toInt = 3
    }

    "set custom player size and grid size" in {
      val configurator = Configurator().withPlayer(5).withGrid(15)
      val config = configurator.build

      config.playerSize should be(5)
      config.gridSize should be(15)
      config.relicSpawnRate should be(15)
      config.echoIncrementer should be(3)
      config.wallRatio should be(5) // (-0.4 * 15 + 11).toInt = 5
    }
  }
}
