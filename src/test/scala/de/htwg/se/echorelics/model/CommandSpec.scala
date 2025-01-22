package de.htwg.se.echorelics.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import scala.util.{Success, Failure}

import service.IGameManager
import model.commandImpl._
import utils.Direction
import model.item.ICard
import model.{ICommand, IFileIO}

class CommandSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "An EchoCommand" should {
    "execute echo on game manager" in {
      val gameManager = mock[IGameManager]
      when(gameManager.echo).thenReturn(gameManager)
      val command = EchoCommand()
      command.execute(gameManager) should be(Success(gameManager))
      verify(gameManager).echo
    }
  }

  "A GridSizeCommand" should {
    "execute setGridSize on game manager" in {
      val gameManager = mock[IGameManager]
      when(gameManager.setGridSize).thenReturn(gameManager)
      val command = GridSizeCommand()
      command.execute(gameManager) should be(Success(gameManager))
      verify(gameManager).setGridSize
    }
  }

  "A MoveCommand" should {
    "execute move on game manager" in {
      val gameManager = mock[IGameManager]
      val direction = Direction.Up
      when(gameManager.move(direction)).thenReturn(gameManager)
      val command = MoveCommand(direction)
      command.execute(gameManager) should be(Success(gameManager))
      verify(gameManager).move(direction)
    }

    "undo move on game manager" in {
      val gameManager = mock[IGameManager]
      val direction = Direction.Up
      when(gameManager.move(direction.opposite)).thenReturn(gameManager)
      val command = MoveCommand(direction)
      command.undo(gameManager) should be(Success(gameManager))
      verify(gameManager).move(direction.opposite)
    }
  }

  "A PauseCommand" should {
    "execute pause on game manager" in {
      val gameManager = mock[IGameManager]
      when(gameManager.pause).thenReturn(gameManager)
      val command = PauseCommand()
      command.execute(gameManager) should be(Success(gameManager))
      verify(gameManager).pause
    }
  }

  "A PlayerSizeCommand" should {
    "execute setPlayerSize on game manager" in {
      val gameManager = mock[IGameManager]
      when(gameManager.setPlayerSize).thenReturn(gameManager)
      val command = PlayerSizeCommand()
      command.execute(gameManager) should be(Success(gameManager))
      verify(gameManager).setPlayerSize
    }
  }

  "A QuitCommand" should {
    "execute quit on game manager" in {
      val gameManager = mock[IGameManager]
      when(gameManager.quit).thenReturn(gameManager)
      val command = QuitCommand()
      command.execute(gameManager) should be(Success(gameManager))
      verify(gameManager).quit
    }
  }

  "A ResumeCommand" should {
    "execute resume on game manager" in {
      val gameManager = mock[IGameManager]
      when(gameManager.resume).thenReturn(gameManager)
      val command = ResumeCommand()
      command.execute(gameManager) should be(Success(gameManager))
      verify(gameManager).resume
    }
  }

  "A StartCommand" should {
    "execute start on game manager" in {
      val gameManager = mock[IGameManager]
      when(gameManager.start).thenReturn(gameManager)
      val command = StartCommand()
      command.execute(gameManager) should be(Success(gameManager))
      verify(gameManager).start
    }
  }

  "fail if no card is found at the given index" in {
    val gameManager = mock[IGameManager]
    when(gameManager.playerCard(0)).thenReturn(None)
    val command = PlayCardCommand(0)
    command.execute(gameManager).isFailure should be(true)
    verify(gameManager).playerCard(0)
  }
}
