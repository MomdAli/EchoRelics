package model.commands

import service.GameManager
import utils.Direction
import scala.util.{Failure, Try}

sealed trait Command {
  def execute(gameManager: GameManager): Try[GameManager]
  def undo(gameManager: GameManager): Try[GameManager] = Failure(
    new RuntimeException("Undo not implemented")
  )
  def redo(gameManager: GameManager): Try[GameManager] = Failure(
    new RuntimeException("Redo not implemented")
  )
}

case class EchoCommand() extends Command {
  override def execute(gameManager: GameManager): Try[GameManager] = {
    Try(gameManager.echo)
  }
}

case class GridSizeCommand() extends Command {
  override def execute(gameManager: GameManager): Try[GameManager] = {
    Try(gameManager.setGridSize)
  }
}

case class MoveCommand(direction: Direction) extends Command {
  override def execute(gameManager: GameManager): Try[GameManager] = {
    Try(gameManager.move(direction))
  }

  override def undo(gameManager: GameManager): Try[GameManager] = {
    Try(gameManager.move(direction.opposite))
  }
}

case class PauseCommand() extends Command {
  override def execute(gameManager: GameManager): Try[GameManager] = {
    Try(gameManager.pause)
  }
}

case class PlayerSizeCommand() extends Command {
  override def execute(gameManager: GameManager): Try[GameManager] = {
    Try(gameManager.setPlayerSize)
  }
}

case class QuitCommand() extends Command {
  override def execute(gameManager: GameManager): Try[GameManager] = {
    Try(gameManager.quit)
  }
}

case class ResumeCommand() extends Command {
  override def execute(gameManager: GameManager): Try[GameManager] = {
    Try(gameManager.resume)
  }
}

case class StartCommand() extends Command {
  override def execute(gameManager: GameManager): Try[GameManager] = {
    Try(gameManager.start)
  }
}

case class PlayCardCommand(index: Int) extends Command {
  override def execute(gameManager: GameManager): Try[GameManager] = {
    Try {
      gameManager.playerCard(index) match {
        case Some(card) =>
          val gm = card.play(gameManager)
          gameManager.currentPlayer.inventory.removeCard(index)
          gm
        case None =>
          throw new RuntimeException("No card found at the given index")
      }
    }
  }
}
