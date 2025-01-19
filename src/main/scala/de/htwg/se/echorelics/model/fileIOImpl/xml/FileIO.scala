package model.fileIOImpl.xml

import scala.util.Try
import scala.xml.{XML, PrettyPrinter}
import service.IGameManager
import model.IFileIO
import utils.FileEncryption

class FileIO extends IFileIO {
  override def load: Try[IGameManager] = {
    val encryptedData =
      scala.io.Source
        .fromFile(IFileIO.filePath + "." + IFileIO.fileExtension)
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
