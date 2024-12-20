package model.entity

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class WallSpec extends AnyWordSpec with Matchers {

  "A Wall" should {

    "have id '#'" in {
      val wall = Wall()
      wall.id should be("#")
    }

    "not be walkable" in {
      val wall = Wall()
      wall.isWalkable should be(false)
    }

    "not be collectable" in {
      val wall = Wall()
      wall.isCollectable should be(false)
    }
  }
}
