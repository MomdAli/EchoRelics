package model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.File
import model.fileIOImpl.xml.FileIO
import model.fileIOImpl.json.FileIO
import service.IGameManager
import utils.{Direction, GameState}
import model.events.GameEvent
import model.entity.IEntity
import model.gridImpl.Grid
import model.item.ICard

import play.api.libs.json.Json

class FileIOSpec extends AnyWordSpec with Matchers {

  "The FileIO" should {

    "return a Failure when trying to load a non-existing file" in {
      val xmlFileIO = model.fileIOImpl.xml.FileIO()
      val jsonFileIO = model.fileIOImpl.json.FileIO()
      val result1 = xmlFileIO.load
      val result2 = jsonFileIO.load

      result1.isFailure shouldBe true
      result2.isFailure shouldBe true
    }

    "save a valid GameManager to an encrypted XML and JSON file" in {
      val xmlFileIO = model.fileIOImpl.xml.FileIO()
      val jsonFileIO = model.fileIOImpl.json.FileIO()
      val gameManager = new MockGameManager
      val file = new File(IFileIO.filePath + "." + IFileIO.fileExtension)

      xmlFileIO.save(gameManager)
      jsonFileIO.save(gameManager)

      file.exists() shouldBe true
      file.length() should be > 0L
    }
  }
}

// Minimal mock
class MockGameManager extends IGameManager {

  override val move: Int = 0

  override val players: List[IEntity] = List()

  override val grid: IGrid = new Grid(10)

  override val event: GameEvent = GameEvent.OnNoneEvent

  override def state: GameState = GameState.NotStarted

  override def toXml = <mockGameManager><data/></mockGameManager>

  override def toJson = Json.obj("mock" -> "data")

  override def move(direction: Direction): IGameManager = this

  override def quit: IGameManager = this

  override def setPlayerSize: IGameManager = this

  override def setGridSize: IGameManager = this

  override def echo: IGameManager = this

  override def start: IGameManager = this

  override def pause: IGameManager = this

  override def resume: IGameManager = this

  override def spawnRelic: IGameManager = this

  override def collectRelic(player: IEntity, relic: IEntity): IGameManager =
    this

  override def playerCard(index: Int): Option[ICard] = None

  override def moveAllEchoes: IGameManager = this

  override def damagePlayer(player: IEntity): IGameManager = this
}
