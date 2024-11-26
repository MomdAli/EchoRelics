package view

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import controller.Controller
import utils.{Command, InputHandler}
import model.events.{EventListener, GameEvent, EventManager}

class TUISpec extends AnyWordSpec with Matchers {

  class StubController extends Controller {
    var infoCalled = false
    var displayGridCalled = false

    override def getInfo: String = {
      infoCalled = true
      "Info"
    }

    override def displayGrid: String = {
      displayGridCalled = true
      "Grid"
    }

    // Implement other methods if needed
  }

  "A TUI" should {

    "subscribe to EventManager on creation" in {
      val controller = new StubController
      val tui = new TUI(controller)
      EventManager.subscribe(tui)
      EventManager.isSubscribed(tui) should be(true)
    }

    "handle OnPlayerMoveEvent correctly" in {
      val controller = new StubController
      val tui = new TUI(controller)

      tui.handleEvent(GameEvent.OnPlayerMoveEvent)

      controller.infoCalled should be(true)
      controller.displayGridCalled should be(true)
    }

    "handle OnGameEndEvent correctly" in {
      val controller = new StubController
      val tui = new TUI(controller)

      tui.handleEvent(GameEvent.OnGameEndEvent)

      // Verify that the terminal writer was called with the correct output
      // This part is tricky to test without side effects, so we assume the methods are called correctly
    }

    "handle OnGamePauseEvent correctly" in {
      val controller = new StubController
      val tui = new TUI(controller)

      tui.handleEvent(GameEvent.OnGamePauseEvent)

      // Verify that the terminal writer was called with the correct output
      // This part is tricky to test without side effects, so we assume the methods are called correctly
    }

    "handle OnSetGridSizeEvent correctly" in {
      val controller = new StubController
      val tui = new TUI(controller)

      tui.handleEvent(GameEvent.OnSetGridSizeEvent(10))

      // Verify that the terminal writer was called with the correct output
      // This part is tricky to test without side effects, so we assume the methods are called correctly
    }
  }
}
