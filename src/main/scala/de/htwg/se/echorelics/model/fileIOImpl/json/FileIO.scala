package model.fileIOImpl.json

import play.api.libs.json._
import scala.util.Try
import service.IGameManager
import model.IFileIO
import utils.FileEncryption

class FileIO extends IFileIO {
  override def load: Try[IGameManager] = {
    val file = new java.io.File(IFileIO.filePath + "." + IFileIO.fileExtension)
    if (!file.exists()) {
      return scala.util.Failure(new java.io.FileNotFoundException("Save file not found"))
    }
    val encryptedData =
      scala.io.Source
        .fromFile(file)
        .mkString
    val jsonString = FileEncryption.decrypt(encryptedData)
    val json = Json.parse(jsonString)
    IGameManager.fromJson(json.as[JsObject])
  }

  override def save(gameManager: IGameManager): Unit = {
    val jsonString = Json.prettyPrint(gameManager.toJson)
    val encryptedData = FileEncryption.encrypt(jsonString)
    val pw =
      new java.io.PrintWriter(IFileIO.filePath + "." + IFileIO.fileExtension)
    pw.write(encryptedData)
    pw.close()
  }
}
