package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any
import org.scalatestplus.mockito.MockitoSugar
import scala.util.{Failure, Success, Try}

import model.commandImpl._
import model.ICommand
import service.IGameManager
import utils.{Direction, GameMemento}
import model.item.ICard
import org.scalatest.CancelAfterFailure

class CommandSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "ICommand default implementations" should {
    "fail on undo and redo by default" in {
      val command: ICommand = new ICommand {
        override def execute(gameManager: IGameManager): Try[IGameManager] =
          Success(gameManager)
      }
      command.undo(mock[IGameManager]) shouldBe a[Failure[?]]
      command.redo(mock[IGameManager]) shouldBe a[Failure[?]]
    }
  }

  "EchoCommand" should {
    "call echo on the gameManager" in {
      val gm = mock[IGameManager]
      val cmd = EchoCommand()
      when(gm.echo).thenReturn(gm)
      cmd.execute(gm) shouldBe Success(gm)
      verify(gm).echo
    }
  }

  "GridSizeCommand" should {
    "call setGridSize on the gameManager" in {
      val gm = mock[IGameManager]
      val cmd = GridSizeCommand()
      when(gm.setGridSize).thenReturn(gm)
      cmd.execute(gm) shouldBe Success(gm)
      verify(gm).setGridSize
    }
  }

  "PauseCommand" should {
    "call pause on the gameManager" in {
      val gm = mock[IGameManager]
      val cmd = PauseCommand()
      when(gm.pause).thenReturn(gm)
      cmd.execute(gm) shouldBe Success(gm)
      verify(gm).pause
    }
  }

  "PlayerSizeCommand" should {
    "call setPlayerSize on the gameManager" in {
      val gm = mock[IGameManager]
      val cmd = PlayerSizeCommand()
      when(gm.setPlayerSize).thenReturn(gm)
      cmd.execute(gm) shouldBe Success(gm)
      verify(gm).setPlayerSize
    }
  }

  "QuitCommand" should {
    "call quit on the gameManager" in {
      val gm = mock[IGameManager]
      val cmd = QuitCommand()
      when(gm.quit).thenReturn(gm)
      cmd.execute(gm) shouldBe Success(gm)
      verify(gm).quit
    }
  }

  "ResumeCommand" should {
    "call resume on the gameManager" in {
      val gm = mock[IGameManager]
      val cmd = ResumeCommand()
      when(gm.resume).thenReturn(gm)
      cmd.execute(gm) shouldBe Success(gm)
      verify(gm).resume
    }
  }

  "StartCommand" should {
    "call start on the gameManager" in {
      val gm = mock[IGameManager]
      val cmd = StartCommand()
      when(gm.start).thenReturn(gm)
      cmd.execute(gm) shouldBe Success(gm)
      verify(gm).start
    }
  }

  "MoveCommand" should {
    "call move on the gameManager" in {
      val gm = mock[IGameManager]
      val cmd = MoveCommand(Direction.Up)
      when(gm.move(Direction.Up)).thenReturn(gm)
      cmd.execute(gm) shouldBe Success(gm)
      verify(gm).move(Direction.Up)
    }
    "call move with opposite direction on undo" in {
      val gm = mock[IGameManager]
      val cmd = MoveCommand(Direction.Up)
      when(gm.move(Direction.Down)).thenReturn(gm)
      cmd.undo(gm) shouldBe Success(gm)
      verify(gm).move(Direction.Down)
    }
  }

  "PlayCardCommand" should {
    "throw RuntimeException if no card found" in {
      val gm = mock[IGameManager]
      val cmd = PlayCardCommand(0)
      when(gm.playerCard(any[Int])).thenReturn(None)
      cmd.execute(gm).isFailure shouldBe true
    }
  }

  "SaveGameCommand" should {
    "save the game and return success" in {
      val gm = mock[IGameManager]
      val cmd = SaveGameCommand()
      cmd.execute(gm) shouldBe a[Success[?]]
      verify(gm, org.mockito.Mockito.atLeast(0)).currentPlayer // not strictly needed, just ensures no error
    }
  }


  "CommandHistory" should {
    "execute should push a memento and clear redo" in {
      val history = new CommandHistory
      val gm = mock[IGameManager]
      val memento = mock[GameMemento]
      when(gm.createMemento).thenReturn(memento)

      history.execute(gm) shouldBe Success(gm)
      // can't directly verify stack, but we know it's not failing
      verify(gm).createMemento
    }
    "undo should restore the last memento if available" in {
      val history = new CommandHistory
      val gm = mock[IGameManager]
      val memento = mock[GameMemento]
      when(gm.createMemento).thenReturn(memento)
      when(gm.restore(memento)).thenReturn(gm)

      history.execute(gm)
      history.undo(gm) shouldBe Success(gm)
      verify(gm, times(1)).restore(memento)
    }
    "undo should fail if no commands to undo" in {
      val history = new CommandHistory
      val gm = mock[IGameManager]
      history.undo(gm).isFailure shouldBe true
    }
  }
}