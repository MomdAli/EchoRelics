package utils

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.text.{Font, Text, TextAlignment}
import scalafx.scene.layout._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.{Color, LinearGradient, Stop}
import scalafx.scene.shape.Rectangle
import scalafx.scene.control.{Label, ScrollPane, Button}
import scalafx.scene.Node
import scala.collection.mutable

import model.{IGrid, ITile}
import model.entity.IEntity
import view.gui.ActionHandler

object Renderer {
  private val eventLogQueue: mutable.Queue[(String, String)] =
    mutable.Queue().empty

  // * Render the grid *
  def render(
      grid: IGrid,
      parentWidth: Double,
      parentHeight: Double,
      padding: Double = 10,
      currentPlayerId: String
  ): GridPane = {
    // Calculate available space for the grid
    val availableWidth = parentWidth - padding * 2
    val availableHeight = parentHeight - padding * 2

    val tileSize = calculateTileSize(grid.size, availableWidth, availableHeight)

    val gridPane = new GridPane {
      alignment = Pos.Center
      hgap = 0
      vgap = 0
      styleClass += "grid"
    }

    for (y <- 0 until grid.size) {
      for (x <- 0 until grid.size) {
        val tile = grid.tileAt(Position(x, y))
        val tilePane = renderTile(tile, tileSize, currentPlayerId, x * 10 + y)
        gridPane.add(tilePane, x, y)
      }
    }

    gridPane
  }

  // * Render a single tile *
  def renderTile(
      tile: ITile,
      tileSize: Double,
      playerId: String,
      seed: Int
  ): StackPane = {
    val rect = new Rectangle {
      width = tileSize
      height = tileSize
      // arcWidth = tileSize / 4 // Rounded corners based on tile size
      // arcHeight = tileSize / 4
    }

    // val (floorIndex, _) = Random(seed).nextInt(4)
    val backgroundImage =
      createImageView(s"/image/tile/Floor-${1}.jpg", tileSize)

    // Optional image
    val imageView = tile.entity match {
      case Some(entity) if IEntity.isPlayer(entity) =>
        createImageView(s"/image/Player-${entity.id}.png", tileSize)
      case Some(wall) if IEntity.isWall(wall) =>
        val (wallIndex, _) = Random(seed).nextInt(7)
        createImageView(s"/image/tile/Wall-${wallIndex + 1}.jpg", tileSize)
      case Some(entity) if IEntity.isRelic(entity) =>
        val (relicIndex, _) = Random(seed).nextInt(2)
        createImageView(s"/image/tile/Relic-${relicIndex + 1}.jpg", tileSize)
      case _ =>
        null
    }

    if (tile.entity.exists(e => IEntity.isPlayer(e) && e.id == playerId)) {
      backgroundImage.setStyle(
        "-fx-effect: innershadow(gaussian, #5C946E, 10, 0.5, 0, 0);"
      )
    }

    val stackPane = new StackPane {
      alignment = Pos.Center
      children = if (imageView != null) {
        if (
          IEntity.isWall(tile.entity.get) || IEntity.isRelic(tile.entity.get)
        ) {
          Seq(backgroundImage, imageView)
        } else {
          Seq(new StackPane {
            children = Seq(backgroundImage, imageView)
          })
        }
      } else Seq(backgroundImage)
      styleClass += "tile"
    }

    stackPane
  }

  def createPauseMenu(actionHandler: ActionHandler): Pane = {
    new BorderPane {
      stylesheets.add(getClass.getResource("/css/design.css").toExternalForm)
      center = new FlowPane {
        columnHalignment = scalafx.geometry.HPos.Center
        orientation = scalafx.geometry.Orientation.Vertical
        vgap = 20
        padding = Insets(20)
        children = Seq(
          new Button {
            text = "Resume"
            onAction = _ => actionHandler.onResumeButton()
            styleClass += "btn-normal"
          },
          new Button {
            text = "Main Menu"
            onAction = _ => actionHandler.onLeaveButton()
            styleClass += "btn-normal"
          },
          new Button {
            text = "Quit"
            onAction = _ => actionHandler.onQuitButton()
            styleClass += "btn-normal"
          }
        )
      }
      // styleClass += "simple-background"
    }
  }

  def clearLog(): Unit = {
    eventLogQueue.clear()
  }

  def renderEventLogSystem(message: String, color: String = "white"): Node = {
    val maxMessages = 6 // Maximum number of messages to display

    // Add the new message to the queue
    eventLogQueue.enqueue((message, color))
    if (eventLogQueue.size > maxMessages) {
      eventLogQueue.dequeue()
    }

    // Create a VBox to hold the messages
    val vbox = new VBox {
      alignment = Pos.TopLeft
      spacing = 5
      padding = Insets(10)
      style =
        "-fx-background-color: #333; -fx-alignment: top_left; -fx-border-color: #81C784; -fx-border-width: 2; -fx-padding: 15;"

      prefWidth = 600 // Set a constant width
      prefHeight = 400 // Set a constant height
    }

    // Add each message as a Label to the VBox
    eventLogQueue.foreach { case (msg, msgColor) =>
      val label = new Label {
        alignment = Pos.TOP_CENTER
        text = msg
        wrapText = true
        maxWidth = 580 // Adjust based on your layout
        style =
          s"-fx-text-fill: $msgColor; -fx-font-size: 16px; -fx-background-color: #444; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;"
      }
      vbox.children.add(label)
    }

    vbox
  }

  // * Render the player size prompt *
  def renderPlayerSizePrompt(size: Int): Node = {

    val backgroundRectangle = new Rectangle {
      width = 600
      height = 200
      arcWidth = 20
      arcHeight = 20
      fill = new LinearGradient(
        endX = 1.0,
        stops = List(
          Stop(0, Color.web("#4CAF50")),
          Stop(1, Color.web("#81C784"))
        )
      )
    }

    val sizeText = new Text {
      text = s"Player Size: $size"
      font = Font.font("Verdana", 32)
      fill = Color.White
      textAlignment = TextAlignment.Center
    }

    val instructionText = new Text {
      text = "Press W or S to adjust"
      font = Font.font("Arial", 20)
      fill = Color.LightGray
      textAlignment = TextAlignment.Center
    }

    val textContainer = new VBox {
      alignment = scalafx.geometry.Pos.Center
      spacing = 5
      children = Seq(sizeText, instructionText)
    }

    new StackPane {
      children = Seq(backgroundRectangle, textContainer)
      style = "-fx-padding: 10;"
    }
  }

  // * Render the player stats *
  def renderStats(players: List[IEntity]): VBox = {
    val listSpacing = 10 // Spacing between each player's stats
    val vbox = new VBox {
      alignment = Pos.TopCenter
      spacing = listSpacing
      padding = Insets(10)
      style =
        "-fx-background-color: #2B2E3A; -fx-border-color: #81C784; -fx-border-width: 2; -fx-padding: 15;"
    }

    players.zipWithIndex.foreach { case (player, index) =>
      val playerBox = new VBox {
        alignment = Pos.CenterLeft
        spacing = 5
        style =
          "-fx-background-color: #414242; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;"
      }

      val header = new Label {
        text = s"Player ${index + 1}"
        style =
          "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #81C784;"
      }

      val healthLabel = new Label {
        text = s"Health: ${player.stats.health}"
        style = "-fx-text-fill: #4CAF50; -fx-font-size: 14px;"
      }

      val scoreLabel = new Label {
        text = s"Score: ${player.stats.score}"
        style = "-fx-text-fill: #FFEB3B; -fx-font-size: 14px;"
      }

      val echoesLabel = new Label {
        text = s"Echoes: ${player.stats.echoes}"
        style = "-fx-text-fill: #F44336; -fx-font-size: 14px;"
      }

      val cardLabels = (0 until 3).map { i =>
        val cardText = player.inventory.cardAt(i) match {
          case Some(card) => s"Card ${i + 1}: [${card.name}]"
          case None       => s"Card ${i + 1}: [Empty]"
        }
        new Label {
          text = cardText
          style = "-fx-text-fill: #E91E63; -fx-font-size: 14px;"
        }
      }

      playerBox.children =
        Seq(header, healthLabel, scoreLabel, echoesLabel) ++ cardLabels
      vbox.children.add(playerBox)
    }

    vbox
  }

  // * Create an ImageView with a given image path and size *
  private def createImageView(
      imagePath: String,
      tileSize: Double
  ): ImageView = {
    new ImageView {
      image = new Image(getClass.getResourceAsStream(imagePath))
      fitWidth = tileSize
      fitHeight = tileSize
      preserveRatio = true
    }
  }

  // * Calculate the tile size based on the grid size and parent dimensions *
  private def calculateTileSize(
      gridSize: Int,
      parentWidth: Double,
      parentHeight: Double,
      scaleFactor: Double = 0.9
  ): Double = {
    // Calculate the maximum tile size that fits within the parent dimensions, scaled down
    val maxWidth = (parentWidth - 10) / gridSize // Subtract padding or gaps
    val maxHeight = (parentHeight - 10) / gridSize
    Math.min(
      maxWidth,
      maxHeight
    ) * scaleFactor - 2 // Subtract gap between tiles
  }
}
