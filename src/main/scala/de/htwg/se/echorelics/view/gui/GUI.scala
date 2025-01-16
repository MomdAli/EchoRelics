package view.gui

import javafx.fxml.{FXMLLoader, FXML}
import javafx.scene.layout.{BorderPane, StackPane, VBox}
import javafx.scene.control.Label
import javafx.scene.{Node, Parent}
import javafx.scene.input.KeyEvent

import scalafx.application.JFXApp3
import scalafx.application.ConditionalFeature.FXML
import scalafx.Includes.jfxParent2sfx
import scalafx.scene.input.KeyCombination
import scalafx.scene.media.AudioClip
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.scene.Scene
import scalafx.stage.Stage

import _root_.controller.Controller
import _root_.model.events.{EventListener, EventManager, GameEvent}
import _root_.service.IGameManager
import _root_.utils.{Renderer, TextRenderer}
import _root_.view.UI
import _root_.utils.NodeFinder

class GUI(controller: Controller) extends JFXApp3 with EventListener {

  EventManager.subscribe(this)
  val audioManager = new AudioManager()
  val actionHandler = new ActionHandler(this, controller)
  val keyHandler = new KeyHandler(actionHandler)
  val rootPane = new StackPane() // Placeholder root pane

  // Equivalent to KeyCombination.NO_MATCH
  private val noKeyCombination: KeyCombination = new KeyCombination(
    javafx.scene.input.KeyCombination.NO_MATCH
  ) {
    def matches(event: scalafx.scene.input.KeyEvent): Boolean = false
  }

  override def start(): Unit = {

    audioManager.loadAudioClip("press", "/sound/UI-Button-Press.mp3")
    audioManager.loadMediaPlayer("background", "/sound/Medieval.mp3")

    stage = new JFXApp3.PrimaryStage {
      title = "Echo Relics"
      icons.add(new javafx.scene.image.Image("/image/icon.png"))
      fullScreen = true
      fullScreenExitHint = ""
      fullScreenExitKey = noKeyCombination
      scene = new Scene(rootPane, 800, 600) {
        onKeyPressed = (event: KeyEvent) => keyHandler.handleKeyInput(event)
      }
    }

    // Starts the application with the menu scene
    switchScene("Menu")
    audioManager.playMediaPlayer("background")
  }

  // Switch to a new scene
  def switchScene(sceneName: String): Unit = {
    val newRoot = loadFXML(sceneName)
    rootPane.getChildren.clear()
    rootPane.getChildren.add(newRoot)
  }

  // Load a scene from FXML
  private def loadFXML(
      sceneName: String
  ): Parent = {
    val loader = new FXMLLoader(getClass.getResource(s"/fxml/$sceneName.fxml"))

    loader.setController(actionHandler)
    loader.load[Parent]()
  }

  override def handleEvent(event: GameEvent): Unit = {
    event match {
      case GameEvent.OnGameEndEvent =>
        audioManager.playMediaPlayer("background")
        switchScene("Menu")
      case GameEvent.OnQuitEvent =>
        close()
      case GameEvent.OnGameStartEvent =>
        audioManager.stopMediaPlayer("background")
        switchScene("Game")
        render(controller.gameManager)
        changeCurrentPlayerStatus("1")
        clearLog()
        renderEventLog("Welcome to Echo Relics!", "#5C946E")
      case GameEvent.OnSetGridSizeEvent(size: Int) =>
        switchScene("GridSize")
        render(controller.gameManager)
      case GameEvent.OnSetPlayerSizeEvent(size: Int) =>
        switchScene("PlayerSize")
        renderPrompt(Renderer.renderPlayerSizePrompt(size))
      case GameEvent.OnInfoEvent(message) =>
        renderEventLog(message, "#fffea4")
      case GameEvent.OnPlayCardEvent(card) =>
        render(controller.gameManager)
        renderEventLog(
          s"Player ${controller.gameManager.currentPlayer.id} played a card!",
          "#5C946E"
        )
      case GameEvent.OnUpdateRenderEvent =>
        render(controller.gameManager)
      case GameEvent.OnLoadSaveEvent =>
        audioManager.stopMediaPlayer("background")
        switchScene("Game")
        render(controller.gameManager)
        renderEventLog("Game loaded!", "#F5F5F5")
      case GameEvent.OnPlayerMoveEvent =>
        changeCurrentPlayerStatus(controller.gameManager.currentPlayer.id)
      case _ =>
    }
  }

  def render(gameManager: IGameManager): Unit = {

    // Render the grid
    val gridContainer = NodeFinder.findNodeById(rootPane, "gridPane")

    val gridrender = Renderer.render(
      gameManager.grid,
      rootPane.getWidth(),
      rootPane.getHeight(),
      currentPlayerId = gameManager.currentPlayer.id
    )

    gridContainer match {
      case Some(node: StackPane) =>
        node.getChildren.clear()
        node.getChildren.add(gridrender)
      case _ =>
    }

    // Render the players stats
    val playerStatsContainer =
      NodeFinder.findNodeById(rootPane, "playerStatsPane")

    val playerStatsRender = Renderer.renderStats(gameManager.players)

    playerStatsContainer match {
      case Some(vbox: VBox) =>
        vbox.getChildren().clear()
        vbox.getChildren().add(playerStatsRender)
      case _ =>
    }
  }

  private def renderPrompt(node: Node): Unit = {
    val promptContainer = NodeFinder.findNodeById(rootPane, "promptLabel")

    promptContainer match {
      case Some(label: Label) =>
        label.setGraphic(node)
      case _ =>
    }
  }

  def close(): Unit = {
    stage.close()
  }

  def renderEventLog(message: String, color: String = "white"): Unit = {
    val eventLogContainer = NodeFinder.findNodeById(rootPane, "eventLogPane")

    val eventLogRender = Renderer.renderEventLogSystem(message, color)

    eventLogContainer match {
      case Some(vbox: VBox) =>
        vbox.getChildren.clear()
        vbox.getChildren.add(eventLogRender)
      case _ =>
    }
  }

  def changeCurrentPlayerStatus(playerId: String): Unit = {
    val currentPlayerContainer =
      NodeFinder.findNodeById(rootPane, "playerStatusLabel")

    currentPlayerContainer match {
      case Some(label: Label) =>
        label.setText(s"Player $playerId's turn")
        label.setStyle("-fx-text-fill: #F4C542;")
      case _ =>
    }
  }

  def clearLog(): Unit = {
    Renderer.clearLog()
    NodeFinder.findNodeById(rootPane, "eventLogPane") match {
      case Some(vbox: VBox) =>
        vbox.getChildren.clear()
      case _ =>
    }
  }
}
