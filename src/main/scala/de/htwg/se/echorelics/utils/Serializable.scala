package utils

import play.api.libs.json.JsObject
import scala.util.Try

trait Serializable[T] {
  def toXml: scala.xml.Node
  def toJson: JsObject
}

trait Deserializable[T] {
  def fromXml(node: scala.xml.Node): Try[T]
  def fromJson(json: JsObject): Try[T]
}
