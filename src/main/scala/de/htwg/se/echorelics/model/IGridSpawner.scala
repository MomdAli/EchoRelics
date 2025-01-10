package model

import model.entity.IEntity
import model.generatorImpl.GridSpawner
import model.config.Config

trait IGridSpawner {
  def spawnRelic(grid: IGrid): IGrid
  def setupStartingGrid(grid: IGrid, players: Seq[IEntity]): IGrid
}
