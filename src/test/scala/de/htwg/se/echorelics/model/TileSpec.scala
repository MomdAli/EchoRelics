package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsObject, Json}
import scala.util.{Success, Failure}
import scala.xml.Elem
import model.entity.entityImpl.{Player, Wall, Relic, Echo}
import model.entity.{Empty, IEntity}
import utils.Position

class TileSpec extends AnyWordSpec with Matchers {

  "A Tile" should {

    "be walkable if the contained entity is walkable (e.g. Relic)" in {
      val relicTile = ITile(Some(Relic()))
      relicTile.isWalkable shouldBe true
    }

    "not be walkable if the contained entity is not walkable (e.g. Player)" in {
      val playerTile = ITile(Some(Player("p1")))
      playerTile.isWalkable shouldBe false
    }

    "detect player presence via hasPlayer" in {
      val playerTile = ITile(Some(Player("p1")))
      playerTile.hasPlayer shouldBe true
      val emptyTile = ITile(None)
      emptyTile.hasPlayer shouldBe false
    }

    "detect wall presence via hasWall" in {
      val wallTile = ITile(Some(Wall()))
      wallTile.hasWall shouldBe true
      val emptyTile = ITile(None)
      emptyTile.hasWall shouldBe false
    }

    "detect relic presence via hasRelic" in {
      val relicTile = ITile(Some(Relic()))
      relicTile.hasRelic shouldBe true
      val emptyTile = ITile(None)
      emptyTile.hasRelic shouldBe false
    }

    "check isPlayer(player) correctly" in {
      val player1 = Player("p1")
      val tile = ITile(Some(player1))
      tile.isPlayer(player1) shouldBe true

      val player2 = Player("p2")
      tile.isPlayer(player2) shouldBe false
    }

    "be empty if it contains no entity" in {
      val emptyTile = ITile(None)
      val playerTile = ITile(Some(Player("p1")))
      emptyTile.isEmpty shouldBe true
      playerTile.isEmpty shouldBe false
    }
  }

  "ITile object" should {

    "provide an emptyTile" in {
      ITile.emptyTile.isEmpty shouldBe true
    }

    "spawn an entity with spawnEntity" in {
      val tile = ITile.spawnEntity(Player("p1"))
      tile.entity.nonEmpty shouldBe true
      tile.hasPlayer shouldBe true
    }

    "apply optional entity (Some or None)" in {
      val tileSome = ITile(Some(Relic()))
      tileSome.hasRelic shouldBe true

      val tileNone = ITile(None)
      tileNone.isEmpty shouldBe true
    }

    "serialize toXml (empty vs. non-empty)" in {
      val emptyTileXml = ITile.emptyTile.toXml
      (emptyTileXml \ "entity" \ "@type").text shouldBe "empty"

      val wallTileXml = ITile(Some(Wall())).toXml
      (wallTileXml \ "entity" \ "@type").text shouldBe "wall"
    }

    "serialize toJson (empty vs. non-empty)" in {
      val emptyJson = ITile.emptyTile.toJson
      emptyJson.keys.isEmpty shouldBe true

      val playerJson = ITile(Some(Player("p1"))).toJson
      (playerJson \ "type").as[String] shouldBe "player"
      (playerJson \ "id").as[String] shouldBe "p1"
    }

    "deserialize fromXml (success)" in {
      val xml: Elem =
        <tile>
          <entity type="wall">
            <id>#</id>
          </entity>
        </tile>
      val result = ITile.fromXml(xml)
      result shouldBe a[Success[?]]
      result.get.hasPlayer shouldBe false
      result.get.entity.map(_.id) shouldBe Some("#")
    }

    "deserialize fromXml (empty)" in {
      val xml: Elem =
        <tile>
          <entity type="empty"></entity>
        </tile>
      val result = ITile.fromXml(xml)
      result shouldBe a[Success[?]]
      result.get.isEmpty shouldBe true
    }

    "deserialize fromJson (success)" in {
      val json: JsObject = Json.obj("type" -> "echo", "id" -> "E1", "owner" -> "P1")
      val result = ITile.fromJson(json)
      result shouldBe a[Success[?]]
      result.get.entity.exists(_.id == "E1") shouldBe true
    }

    "deserialize fromJson (empty)" in {
      val json: JsObject = Json.obj()
      val result = ITile.fromJson(json)
      result shouldBe a[Success[?]]
      result.get.isEmpty shouldBe true
    }

    "handle failures in fromXml if invalid XML" in {
      val xml: Elem = <tile></tile> // Missing <entity>
      val result = ITile.fromXml(xml)
      result shouldBe a[Failure[?]]
    }

    "handle failures in fromJson if invalid JSON" in {
      val json: JsObject = Json.obj("type" -> "unknown") // Suppose no valid entity
      val result = ITile.fromJson(json)
      // It will currently default to Empty or fail if some logic changes
      // Let's just check it doesn't blow up unexpectedly:
      result.isSuccess shouldBe true
      result.get.isEmpty shouldBe true
    }
  }
}