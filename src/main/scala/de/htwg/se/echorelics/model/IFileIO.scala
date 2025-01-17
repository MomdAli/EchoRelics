package model

import service.IGameManager
import scala.util.Try

trait IFileIO {
  def load: Try[IGameManager]
  def save(gameManager: IGameManager): Unit

}
object IFileIO {
  val filePath: String = "save"
  val fileExtension: String = "dat"
}
