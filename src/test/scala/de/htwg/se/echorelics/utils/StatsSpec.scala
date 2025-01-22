package utils

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json
import scala.xml.XML

class StatsSpec extends AnyWordSpec with Matchers {

  "A Stats instance" should {

    "increase the score correctly" in {
      val s = Stats().increaseScore(10)
      s.score shouldBe 10
    }

    "not exceed maximum echoes when increased" in {
      val s = Stats(echoes = 2).increaseEchoes(5)
      s.echoes shouldBe 3
    }

    "not exceed maximum health when increased" in {
      val s = Stats(health = 4).increaseHealth(2)
      s.health shouldBe 5
    }

    "not drop below zero score when decreased" in {
      val s = Stats(score = 3).decreaseScore(5)
      s.score shouldBe 0
    }

    "not drop below zero echoes when decreased" in {
      val s = Stats(echoes = 3).decreaseEchoes(4)
      s.echoes shouldBe 0
    }

    "not drop below zero health when decreased" in {
      val s = Stats(health = 2).decreaseHealth(5)
      s.health shouldBe 0
    }

    "serialize to JSON properly" in {
      val s = Stats(10, 2, 5)
      val json = s.toJson
      (json \ "score").as[Int] shouldBe 10
      (json \ "echoes").as[Int] shouldBe 2
      (json \ "health").as[Int] shouldBe 5
    }

    "deserialize from JSON properly" in {
      val json = Json.obj("score" -> 10, "echoes" -> 2, "health" -> 5)
      val s = Stats.fromJson(json).get
      s.score shouldBe 10
      s.echoes shouldBe 2
      s.health shouldBe 5
    }

    "serialize to XML properly" in {
      val s = Stats(7, 1, 3)
      val xml = s.toXml
      (xml \ "score").text.toInt shouldBe 7
      (xml \ "echoes").text.toInt shouldBe 1
      (xml \ "health").text.toInt shouldBe 3
    }

    "deserialize from XML properly" in {
      val xml =
        <stats><score>7</score><echoes>1</echoes><health>3</health></stats>
      val s = Stats.fromXml(xml).get
      s.score shouldBe 7
      s.echoes shouldBe 1
      s.health shouldBe 3
    }
  }
}
