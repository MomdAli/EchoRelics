package view.gui

import java.nio.file.{Files, Paths}

import javafx.fxml.{FXMLLoader, FXML}
import javafx.scene.layout.{BorderPane, StackPane, VBox}
import javafx.scene.control.Label
import javafx.scene.{Node, Parent}
import javafx.scene.input.KeyEvent
import javafx.scene.control.Button

import scalafx.application.JFXApp3
import scalafx.application.ConditionalFeature.FXML
import scalafx.Includes.jfxParent2sfx
import scalafx.scene.input.KeyCombination
import scalafx.collections.CollectionIncludes.observableList2ObservableBuffer
import scalafx.scene.media.{Media, MediaPlayer, AudioClip}
import scalafx.scene.Scene
import scalafx.stage.Stage
import scalafx.scene.effect.GaussianBlur
import scalafx.scene.layout.Pane

import _root_.controller.Controller
import _root_.model.events.{EventListener, EventManager, GameEvent}
import _root_.service.IGameManager
import _root_.utils.{Renderer, TextRenderer}
import _root_.utils.NodeFinder
import scala.annotation.switch
import model.IFileIO

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

    audioManager.loadAudioClip("press", "/sound/UI-Select.wav")
    audioManager.loadAudioClip("hurt", "/sound/Player-Hurt.mp3")
    audioManager.loadAudioClip("use", "/sound/Item-Use.mp3")
    audioManager.loadAudioClip("collect", "/sound/Relic-Collect.mp3")
    audioManager.loadAudioClip("step", "/sound/Step.wav")
    audioManager.loadMediaPlayer("background", "/sound/Medieval.mp3")

    stage = new JFXApp3.PrimaryStage {
      title = "Echo Relics"
      icons.add(new javafx.scene.image.Image("/image/icon.png"))
      fullScreen = true
      fullScreenExitHint = ""
      fullScreenExitKey = noKeyCombination
      scene = new Scene(rootPane, 800, 600) {
        rootPane.setFocusTraversable(false)
        onKeyPressed = (event: KeyEvent) => keyHandler.handleKeyInput(event)
      }
    }

    // Starts the application with the menu scene
    switchScene("Menu")
    configureContinueButton()
    audioManager.playMediaPlayer("background")
  }

  // Switch to a new scene
  def switchScene(sceneName: String): Unit = {
    val newRoot = loadFXML(sceneName)
    rootPane.setFocusTraversable(false)
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
        configureContinueButton()
      case GameEvent.OnQuitEvent =>
        close()
      case GameEvent.OnGameStartEvent =>
        audioManager.stopMediaPlayer("background")
        switchScene("Game")
        render(controller.gameManager)
        changeCurrentPlayerStatus("1")
        changeCurrentPlayerStatus(controller.gameManager.currentPlayer.id)
        clearLog()
        renderEventLog("Welcome to Echo Relics!", "#5C946E")
        showTutorialScreen()
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
        audioManager.playAudioClip("use")
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
        changeCurrentPlayerStatus(controller.gameManager.currentPlayer.id)
        renderEventLog("Game loaded!", "#F5F5F5")
      case GameEvent.OnPlayerMoveEvent =>
        changeCurrentPlayerStatus(controller.gameManager.currentPlayer.id)
        audioManager.playAudioClip("step")
      case GameEvent.OnPlayerDamageEvent(player) =>
        render(controller.gameManager)
        audioManager.playAudioClip("hurt")
        renderEventLog(
          s"Player ${player.id} was damaged!",
          "#E9A2A1"
        )
      case GameEvent.OnRelicCollectEvent(player, relic) =>
        audioManager.playAudioClip("collect")
      case GameEvent.OnGamePauseEvent =>
        showPauseMenu()
      case GameEvent.OnGameResumeEvent =>
        hidePauseMenu()
      case GameEvent.OnWinnerEvent(playerId) =>
        showWinnerMenu(playerId)
      case GameEvent.OnEchoSpawnEvent =>
        audioManager.playAudioClip("use")
      case GameEvent.OnErrorEvent(message) =>
        renderEventLog(message, "#ff0000")
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
        label.setText(
          s"Player $playerId's turn \nRound ${controller.gameManager.round}"
        )
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

  def showWinnerMenu(winnerId: String): Unit = {
    val winnerMenu = Renderer.createWinnerScreen(winnerId, actionHandler)

    rootPane.getChildren.add(winnerMenu)
  }

  def showPauseMenu(): Unit = {
    // Create the pause menu using Renderer
    val pauseMenu = Renderer.createPauseMenu(actionHandler)

    // Style and bind size to make it fullscreen
    pauseMenu.setStyle(
      "-fx-background-color: rgba(0, 0, 0, 0.7);"
    ) // Semi-transparent
    pauseMenu.prefWidthProperty().bind(rootPane.widthProperty())
    pauseMenu.prefHeightProperty().bind(rootPane.heightProperty())

    rootPane.getChildren.headOption.foreach(_.setEffect(new GaussianBlur(10)))

    rootPane.getChildren.add(pauseMenu)
  }

  def showTutorialScreen(): Unit = {
    val tutorialScreen = Renderer.createTutorialScreen(actionHandler)
    rootPane.getChildren.add(tutorialScreen)
  }

  def hideTutorialScreen(): Unit = {
    rootPane.getChildren.removeIf {
      case node: javafx.scene.layout.Pane =>
        node.getStyle.contains(
          "rgba(0, 0, 0, 0.9);"
        ) // Match the tutorial screen's background
      case _ => false
    }
  }

  def hidePauseMenu(): Unit = {
    rootPane.getChildren.headOption.foreach(_.setEffect(null))

    rootPane.getChildren.removeIf {
      case node: javafx.scene.layout.Pane =>
        node.getStyle.contains("rgba(0, 0, 0, 0.7);")
      case _ => false
    }
  }

  private def configureContinueButton(): Unit = {
    // Find the Continue button in the Menu.fxml
    val continueButtonOption =
      NodeFinder.findNodeById(rootPane, "continueButton")
    continueButtonOption match {
      case Some(button: Button) =>
        // Check if the save file exists and disable the button if not
        val saveFilePath =
          Paths.get(IFileIO.filePath + "." + IFileIO.fileExtension)
        button.setDisable(!Files.exists(saveFilePath))
      case _ =>
        println("Continue button not found in Menu.fxml.")
    }
  }
}
