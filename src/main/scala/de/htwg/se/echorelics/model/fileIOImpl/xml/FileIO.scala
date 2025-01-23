package model.fileIOImpl.xml

import scala.util.Try
import scala.xml.{XML, PrettyPrinter}
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
    val xmlString = FileEncryption.decrypt(encryptedData)
    IGameManager.fromXml(XML.loadString(xmlString))
  }

  override def save(gameManager: IGameManager): Unit = {
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xmlString = prettyPrinter.format(gameManager.toXml)
    val encryptedData = FileEncryption.encrypt(xmlString)
    val pw =
      new java.io.PrintWriter(IFileIO.filePath + "." + IFileIO.fileExtension)
    pw.write(encryptedData)
    pw.close()
  }
}
