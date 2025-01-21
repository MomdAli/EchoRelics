package utils

import play.api.libs.json.{JsObject, Json}
import scala.util.Try

case class Stats(
    val score: Int = 0,
    val echoes: Int = 0,
    val health: Int = 3
) extends Serializable[Stats] {

  def increaseScore(amount: Int): Stats = copy(score = score + amount)
  def increaseEchoes(amount: Int): Stats =
    copy(echoes = math.min(3, echoes + amount))
  def increaseHealth(amount: Int): Stats =
    copy(health = math.min(5, health + amount))

  def decreaseScore(amount: Int): Stats =
    copy(score = math.max(0, score - amount))
  def decreaseEchoes(amount: Int): Stats =
    copy(echoes = math.max(0, echoes - amount))
  def decreaseHealth(amount: Int): Stats =
    copy(health = math.max(0, health - amount))

  override def toString(): String = {
    s"Score: $score\nEchoes: $echoes\nHealth: $health"
  }

  override def toXml = {
    <stats>
      <score>{score}</score>
      <echoes>{echoes}</echoes>
      <health>{health}</health>
    </stats>
  }

  override def toJson = {
    Json.obj(
      "score" -> score,
      "echoes" -> echoes,
      "health" -> health
    )
  }
}

object Stats extends Deserializable[Stats] {
  override def fromXml(node: scala.xml.Node): Try[Stats] = Try {
    Stats(
      score = (node \ "score").text.toInt,
      echoes = (node \ "echoes").text.toInt,
      health = (node \ "health").text.toInt
    )
  }

  override def fromJson(json: JsObject): Try[Stats] = Try {
    Stats(
      score = (json \ "score").as[Int],
      echoes = (json \ "echoes").as[Int],
      health = (json \ "health").as[Int]
    )
  }
}
