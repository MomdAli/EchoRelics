package model.commandImpl

import service.IGameManager
import utils.Direction
import scala.util.{Failure, Try}
import model.item.ICard
import model.ICommand
import modules.Default.given
import model.IFileIO

case class EchoCommand() extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    Try(gameManager.echo)
  }
}

case class GridSizeCommand() extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    Try(gameManager.setGridSize)
  }
}

case class MoveCommand(direction: Direction) extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    Try(gameManager.move(direction))
  }

  override def undo(gameManager: IGameManager): Try[IGameManager] = {
    Try(gameManager.move(direction.opposite))
  }
}

case class PauseCommand() extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    Try(gameManager.pause)
  }
}

case class PlayerSizeCommand() extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    Try(gameManager.setPlayerSize)
  }
}

case class QuitCommand() extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    Try(gameManager.quit)
  }
}

case class ResumeCommand() extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    Try(gameManager.resume)
  }
}

case class StartCommand() extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    Try(gameManager.start)
  }
}

case class PlayCardCommand(index: Int) extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    Try {
      gameManager.playerCard(index) match {
        case Some(card: ICard) =>
          val gm = card.play(gameManager)
          gameManager.currentPlayer.inventory.removeCard(index)
          gm
        case None =>
          throw new RuntimeException("No card found at the given index")
      }
    }
  }
}

case class SaveGameCommand() extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    summon[IFileIO].save(gameManager)
    Try(gameManager)
  }
}

case class LoadGameCommand() extends ICommand {
  override def execute(gameManager: IGameManager): Try[IGameManager] = {
    summon[IFileIO].load
  }
}
