package service.serviceImpl

import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions._

import model.events.GameEvent
import model.{IGrid, ICommand}
import model.entity.IEntity
import utils.{Direction, GameState}
import model.item.ICard
import service.IGameManager
import model.IFileIO

case class WinnerManager(
    move: Int,
    players: List[IEntity],
    grid: IGrid,
    event: GameEvent
) extends IGameManager {

  override def state: GameState = GameState.Victory

  def leaveMenu(): IGameManager = {
    IFileIO.deleteSaveFile()
    MenuManager(
      move = 0,
      players,
      injector.instance[IGrid](Names.named("Small")),
      GameEvent.OnGameEndEvent
    )
  }

  override def quit: IGameManager = {
    leaveMenu()
  }

  override def echo: IGameManager = {
    leaveMenu()
  }

  override def start: IGameManager = {
    leaveMenu()
  }

  override def pause: IGameManager = {
    leaveMenu()
  }

}
