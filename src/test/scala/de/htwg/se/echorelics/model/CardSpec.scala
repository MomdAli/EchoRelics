package model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.{JsObject, Json}
import model.item.itemImpl._
import model.item._
import model.events._
import service.IGameManager
import model.entity.IEntity
import utils.{Stats}

class CardSpec extends AnyWordSpec with Matchers {

  // Minimal test Player that can heal / updateStats
  case class TestPlayer(id: String, override val stats: Stats) extends IEntity {
    override def isWalkable: Boolean = false
    override def isCollectable: Boolean = false
    override def score: Int = stats.score
    override def heal: TestPlayer = copy(stats = stats.copy(health = stats.health + 1))
    override def updateStats(newStats: Stats): IEntity = copy(stats = newStats)
    override def toJson: JsObject = Json.obj("id" -> id, "stats" -> stats.toJson)
    override def toXml: scala.xml.Node = <player><id>{id}</id><stats>{stats.toXml}</stats></player>
  }
  case class TestManager(
      move: Int,
      players: List[TestPlayer],
      currentIndex: Int
  ) extends IGameManager {
    override def currentPlayer: TestPlayer = players(currentIndex)
    override val grid: IGrid = null // not needed for these tests
    override def change(
        newMove: Int,
        newPlayers: List[IEntity],
        newGrid: IGrid,
        event: GameEvent
    ): IGameManager = this.copy(move = newMove, players = newPlayers.map(_.asInstanceOf[TestPlayer]))
    override def playerCard(index: Int): Option[ICard] = None
    override val event: GameEvent = null
    override def state: utils.GameState = null
  }

  "ICard" should {
    "have correct toScore, toXml, and toJson" in {
      val card = new ICard {
        override val rarity = Rarity.Common
        override val name = "TestCard"
        override val description = "Just a test"
        override def play(gm: IGameManager) = gm
      }
      card.toScore shouldBe Rarity.Common.value
      card.toXml.toString should include ("<name>TestCard</name>")
      card.toJson.toString should include ("\"name\":\"TestCard\"")
    }

    "load fromXml and fromJson properly using ICard object" in {
      val xml = <card><name>Heal</name></card>
      val json: JsObject = Json.obj("name" -> "Strike")
      ICard.fromXml(xml).get.name shouldBe "Heal"
      ICard.fromJson(json).get.name shouldBe "Strike"
      ICard.cards.map(_.name) should contain ("Swap Player")
    }
  }

  "HealCard" should {
    "increase health of current player" in {
      val p1 = TestPlayer("p1", Stats(0,0,5))
      val gm = TestManager(3, List(p1), 0)
      val newGm = HealCard().play(gm)
      newGm.asInstanceOf[TestManager].players.head.stats.health shouldBe 6
    }
  }

  "SwapPlayerCard" should {
    "do nothing if only one player" in {
      val p1 = TestPlayer("p1", Stats())
      val gm = TestManager(0, List(p1), 0)
      val newGm = SwapPlayerCard().play(gm)
      newGm shouldBe gm
    }
  }

  "TimeTravelCard" should {
    "notify event but otherwise not change the manager" in {
      val p1 = TestPlayer("p1", Stats())
      val gm = TestManager(1, List(p1), 0)
      val newGm = TimeTravelCard().play(gm)
      newGm shouldBe gm
    }
  }

  "StrikeCard" should {
    "deal damage to other players but not current" in {
      val p1 = TestPlayer("p1", Stats(0,0,10))
      val p2 = TestPlayer("p2", Stats(0,0,10))
      val gm = TestManager(0, List(p1,p2), 0)
      val newGm = StrikeCard().play(gm)
      val newPlayers = newGm.asInstanceOf[TestManager].players
      newPlayers.head.stats.health shouldBe 10 // unchanged
      newPlayers(1).stats.health shouldBe 9    // took damage
    }
    "do nothing if only one player" in {
      val p1 = TestPlayer("p1", Stats(0,0,5))
      val gm = TestManager(0, List(p1), 0)
      val newGm = StrikeCard().play(gm)
      newGm.shouldBe(gm)
    }
  }
}