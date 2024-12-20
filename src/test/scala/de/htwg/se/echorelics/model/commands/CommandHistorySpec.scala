package model.commands

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import service.GameManager

class CommandHistorySpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A CommandHistory" should {

    "save the state correctly" in {
      val gameManager = mock[GameManager]
      val commandHistory = new CommandHistory

      commandHistory.saveState(gameManager)

      verify(gameManager).createMemento
      commandHistory.undo(gameManager).isDefined should be(true)
    }

    "undo the last action correctly" in {
      val gameManager = mock[GameManager]
      val memento = mock[GameMemento]
      val commandHistory = new CommandHistory

      when(gameManager.createMemento).thenReturn(memento)
      when(gameManager.restore(memento)).thenReturn(gameManager)

      commandHistory.saveState(gameManager)
      val result = commandHistory.undo(gameManager)

      result should be(Some(gameManager))
      verify(gameManager).restore(memento)
    }

    "redo the last undone action correctly" in {
      val gameManager = mock[GameManager]
      val memento = mock[GameMemento]
      val commandHistory = new CommandHistory

      when(gameManager.createMemento).thenReturn(memento)
      when(gameManager.restore(memento)).thenReturn(gameManager)

      commandHistory.saveState(gameManager)
      commandHistory.undo(gameManager)
      val result = commandHistory.redo(gameManager)

      result should be(Some(gameManager))
      verify(gameManager, times(2)).restore(memento)
    }

    "not undo if there is no history" in {
      val gameManager = mock[GameManager]
      val commandHistory = new CommandHistory

      val result = commandHistory.undo(gameManager)

      result should be(None)
    }

    "not redo if there is no history" in {
      val gameManager = mock[GameManager]
      val commandHistory = new CommandHistory

      val result = commandHistory.redo(gameManager)

      result should be(None)
    }

    "clear redo history on saveState" in {
      val gameManager = mock[GameManager]
      val memento = mock[GameMemento]
      val commandHistory = new CommandHistory

      when(gameManager.createMemento).thenReturn(memento)
      when(gameManager.restore(memento)).thenReturn(gameManager)

      commandHistory.saveState(gameManager)
      commandHistory.undo(gameManager)
      commandHistory.saveState(gameManager)

      val result = commandHistory.redo(gameManager)

      result should be(None)
    }
  }
}
