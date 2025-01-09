package utils

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.text.{Font, Text, TextAlignment}
import scalafx.scene.layout.{GridPane, StackPane, Pane, VBox}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.{Color, LinearGradient, Stop}
import scalafx.scene.shape.Rectangle
import scalafx.scene.control.{Label}
import scalafx.scene.Node

import model.{IGrid, ITile}
import model.entity.IEntity

object Renderer {

  // * Render the grid *
  def render(
      grid: IGrid,
      parentWidth: Double,
      parentHeight: Double,
      padding: Double = 10
  ): GridPane = {
    // Calculate available space for the grid
    val availableWidth = parentWidth - padding * 2
    val availableHeight = parentHeight - padding * 2

    val tileSize = calculateTileSize(grid.size, availableWidth, availableHeight)

    val gridPane = new GridPane {
      alignment = Pos.Center
      hgap = 2
      vgap = 2
      styleClass += "grid"
    }

    for (y <- 0 until grid.size) {
      for (x <- 0 until grid.size) {
        val tile = grid.tileAt(Position(x, y))
        val tilePane = renderTile(tile, tileSize)
        gridPane.add(tilePane, x, y)
      }
    }

    gridPane
  }

  // * Render a single tile *
  def renderTile(tile: ITile, tileSize: Double): StackPane = {
    val rect = new Rectangle {
      width = tileSize
      height = tileSize
      arcWidth = tileSize / 4 // Rounded corners based on tile size
      arcHeight = tileSize / 4

      fill = tile.entity match {
        case Some(entity) if IEntity.isPlayer(entity) =>
          new LinearGradient(
            0,
            0,
            1,
            1,
            true,
            null,
            List(
              Stop(0, Color.Blue),
              Stop(1, Color.LightBlue)
            )
          ) // Gradient for Player
        case Some(entity) if IEntity.isWall(entity) =>
          Color.DarkGray
        case Some(entity) if IEntity.isRelic(entity) =>
          Color.Gold
        case Some(entity) if IEntity.isEcho(entity) =>
          Color.OrangeRed
        case _ =>
          Color.White
      }
    }

    // Optional image
    val imageView = tile.entity match {
      case Some(entity) if IEntity.isPlayer(entity) =>
        createImageView(s"/image/Player-${entity.id}.png", tileSize)
      case _ =>
        null
    }

    val stackPane = new StackPane {
      alignment = Pos.Center
      children = if (imageView != null) Seq(rect, imageView) else Seq(rect)
      styleClass += "tile"
    }

    stackPane
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
      scaleFactor: Double = 0.75
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
