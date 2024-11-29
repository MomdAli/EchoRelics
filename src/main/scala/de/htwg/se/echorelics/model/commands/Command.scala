package model.commands

import service.GameManager
import utils.Direction

sealed trait Command {
  def execute(gameManager: GameManager): GameManager
}

case class EchoCommand() extends Command {
  override def execute(gameManager: GameManager): GameManager = {
    gameManager.echo
  }
}

case class GridSizeCommand() extends Command {
  override def execute(gameManager: GameManager): GameManager = {
    gameManager.setGridSize
  }
}

case class MoveCommand(direction: Direction) extends Command {
  override def execute(gameManager: GameManager): GameManager = {
    direction match {
      case Direction.Up    => gameManager.moveUp
      case Direction.Down  => gameManager.moveDown
      case Direction.Left  => gameManager.moveLeft
      case Direction.Right => gameManager.moveRight
    }
  }
}

case class PauseCommand() extends Command {
  override def execute(gameManager: GameManager): GameManager = {
    gameManager.pause
  }
}

case class PlayerSizeCommand() extends Command {
  override def execute(gameManager: GameManager): GameManager = {
    gameManager.setPlayerSize
  }
}

case class QuitCommand() extends Command {
  override def execute(gameManager: GameManager): GameManager = {
    gameManager.quit
  }
}

case class ResumeCommand() extends Command {
  override def execute(gameManager: GameManager): GameManager = {
    gameManager.resume
  }
}

case class StartCommand() extends Command {
  override def execute(gameManager: GameManager): GameManager = {
    gameManager.start
  }
}

case class PlayCardCommand(index: Int) extends Command {
  override def execute(gameManager: GameManager): GameManager = {
    gameManager.playerCard(index) match {
      case Some(card) => {
        val gm = card.play(gameManager)
        gameManager.currentPlayer.inventory.removeCard(index)
        gm
      }
      case None => gameManager
    }
  }
}
