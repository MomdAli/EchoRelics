package model.entity

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import scala.xml.Utility.trim

import model.entity.entityImpl._

class EntitySpec extends AnyWordSpec with Matchers {

  "An Empty entity" should {
    val empty = Empty()

    "have an id of ' '" in {
      empty.id should be(" ")
    }

    "be walkable" in {
      empty.isWalkable should be(true)
    }

    "not be collectable" in {
      empty.isCollectable should be(false)
    }

    "serialize to XML correctly" in {
      trim(empty.toXml) should be(trim(<entity type="empty"></entity>))
    }

    "serialize to JSON correctly" in {
      empty.toJson should be(Json.obj())
    }
  }

  "A Player entity" should {
    val player = Player(id = "player1")

    "have the correct id" in {
      player.id should be("player1")
    }

    "not be walkable" in {
      player.isWalkable should be(false)
    }

    "not be collectable" in {
      player.isCollectable should be(false)
    }

    "serialize to XML correctly" in {
      trim(player.toXml) should be(
        trim(
          <entity type="player">
                    <id>player1</id>
                    {player.stats.toXml}
                    {player.inventory.toXml}
                </entity>
        )
      )
    }

    "serialize to JSON correctly" in {
      player.toJson should be(
        Json.obj(
          "type" -> "player",
          "id" -> "player1",
          "stats" -> player.stats.toJson,
          "inventory" -> player.inventory.toJson
        )
      )
    }
  }

  "A Relic entity" should {
    val relic = Relic()

    "have the correct id" in {
      relic.id should be("$")
    }

    "be walkable" in {
      relic.isWalkable should be(true)
    }

    "be collectable" in {
      relic.isCollectable should be(true)
    }

    "serialize to XML correctly" in {
      trim(relic.toXml) should be(
        trim(
          <entity type="relic">
                    <id>$</id>
                    <score>{relic.score}</score>
                </entity>
        )
      )
    }

    "serialize to JSON correctly" in {
      relic.toJson should be(
        Json.obj(
          "type" -> "relic",
          "id" -> "$",
          "score" -> relic.score
        )
      )
    }
  }

  "An Echo entity" should {
    val echo = Echo(id = "e1", owner = "player1")

    "have the correct id" in {
      echo.id should be("e1")
    }

    "have the correct owner" in {
      echo.owner should be("player1")
    }

    "not be walkable" in {
      echo.isWalkable should be(false)
    }

    "not be collectable" in {
      echo.isCollectable should be(false)
    }

    "serialize to XML correctly" in {
      trim(echo.toXml) should be(
        trim(
          <entity type="echo">
                    <id>e1</id>
                    <owner>player1</owner>
                </entity>
        )
      )
    }

    "serialize to JSON correctly" in {
      echo.toJson should be(
        Json.obj(
          "type" -> "echo",
          "id" -> "e1",
          "owner" -> "player1"
        )
      )
    }
  }

  "A Wall entity" should {
    val wall = Wall()

    "have the correct id" in {
      wall.id should be("#")
    }

    "not be walkable" in {
      wall.isWalkable should be(false)
    }

    "not be collectable" in {
      wall.isCollectable should be(false)
    }

    "serialize to XML correctly" in {
      trim(wall.toXml) should be(
        trim(
          <entity type="wall">
                    <id>#</id>
                </entity>
        )
      )
    }

    "serialize to JSON correctly" in {
      wall.toJson should be(
        Json.obj(
          "type" -> "wall",
          "id" -> "#"
        )
      )
    }
  }
}
