package de.htwg.se.echorelics.modules

import model._
import model.config.Configurator

object Default {
  given IGrid = new gridImpl.Grid(10)
  given IGridSpawner = new generatorImpl.GridSpawner(Configurator.default)
  given item.IInventory = new item.inventoryImpl.Inventory
}
