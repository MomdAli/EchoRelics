package modules

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import model._
import model.fileIOImpl.json.FileIO
import model.config.Configurator

class DefaultSpec extends AnyWordSpec with Matchers {

  "The Default module" should {

    "provide a default grid" in {
      Default.given_IGrid shouldBe a[gridImpl.Grid]
      Default.given_IGrid.size shouldBe 10
    }

    "provide a default grid spawner" in {
      Default.given_IGridSpawner shouldBe a[generatorImpl.GridSpawner]
      Default.given_IGridSpawner shouldBe a[generatorImpl.GridSpawner]
    }

    "provide a default inventory" in {
      Default.given_IInventory shouldBe a[item.inventoryImpl.Inventory]
      Default.given_IInventory.isFull shouldBe false
    }

    "provide a default file IO" in {
      Default.given_IFileIO shouldBe a[FileIO]
    }
  }
}
