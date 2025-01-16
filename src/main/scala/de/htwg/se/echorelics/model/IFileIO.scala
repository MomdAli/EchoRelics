package model

import service.IGameManager
import scala.util.Try

trait IFileIO {
  def load: Try[IGameManager]
  def save(gameManager: IGameManager): Unit

  val filePath: String = "echorelics"
  val fileExtension: String = "dat"
}
