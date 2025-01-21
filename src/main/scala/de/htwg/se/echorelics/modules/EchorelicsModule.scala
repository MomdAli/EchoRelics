package modules

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import net.codingwell.scalaguice.ScalaModule

import model._
import service.IGameManager
import utils.{Direction, Stats}

class EchorelicsModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {

    bind[IGameManager]
      .annotatedWithName("Menu")
      .toProvider(() =>
        new service.serviceImpl.MenuManager(
          move = 0,
          players =
            List(entity.entityImpl.Player("1"), entity.entityImpl.Player("2")),
          grid = new gridImpl.Grid(Vector.fill(10, 10)(ITile.emptyTile)),
          event = events.GameEvent.OnGameStartEvent // Adjust as needed
        )
      )

    bind[IGrid].toProvider(() =>
      new gridImpl.Grid(Vector.fill(10, 10)(ITile.emptyTile))
    )

    bind[IGrid]
      .annotatedWithName("Small")
      .toProvider(() => new gridImpl.Grid(Vector.fill(10, 10)(ITile.emptyTile)))

    bind[IGrid]
      .annotatedWithName("Medium")
      .toProvider(() => new gridImpl.Grid(Vector.fill(15, 15)(ITile.emptyTile)))

    bind[IGrid]
      .annotatedWithName("Large")
      .toProvider(() => new gridImpl.Grid(Vector.fill(20, 20)(ITile.emptyTile)))

    bind[IGridSpawner].toProvider(() =>
      new generatorImpl.GridSpawner(config.Configurator.default)
    )

    // bind[item.IInventory].toProvider(() =>
    //   new item.inventoryImpl.Inventory(maxCards = 3)
    // )

    bind[item.IInventory].toInstance(
      new item.inventoryImpl.Inventory(maxCards = 3)
    )

    bind[Stats].toProvider(() => Stats(0, 0, 3))

    bind[ITile]
      .annotatedWithName("EmptyTile")
      .toProvider(() => tileImpl.Tile(entity = None))

    bind[item.ICard].annotatedWithName("HealCard").to[item.itemImpl.HealCard]
    bind[item.ICard]
      .annotatedWithName("StrikeCard")
      .to[item.itemImpl.StrikeCard]
    bind[item.ICard]
      .annotatedWithName("SwapPlayerCard")
      .to[item.itemImpl.SwapPlayerCard]
    bind[item.ICard]
      .annotatedWithName("TimeTravelCard")
      .to[item.itemImpl.TimeTravelCard]

    bind[entity.IEntity]
      .annotatedWithName("Player")
      .toProvider[PlayerProvider]

    bind[String]
      .annotatedWith(Names.named("DefaultOwner"))
      .toInstance("1")

    bind[entity.IEntity]
      .annotatedWithName("Echo")
      .toProvider(() => new entity.entityImpl.Echo(id = "e"))
    bind[entity.IEntity].annotatedWithName("Relic").to[entity.entityImpl.Relic]
    bind[entity.IEntity].annotatedWithName("Wall").to[entity.entityImpl.Wall]

    bind[ICommand]
      .annotatedWithName("MoveUp")
      .toInstance(commandImpl.MoveCommand(Direction.Up))
    bind[ICommand]
      .annotatedWithName("MoveDown")
      .toInstance(commandImpl.MoveCommand(Direction.Down))
    bind[ICommand]
      .annotatedWithName("MoveLeft")
      .toInstance(commandImpl.MoveCommand(Direction.Left))
    bind[ICommand]
      .annotatedWithName("MoveRight")
      .toInstance(commandImpl.MoveCommand(Direction.Right))
    bind[ICommand]
      .annotatedWithName("PlayCard0")
      .toInstance(commandImpl.PlayCardCommand(0))
    bind[ICommand]
      .annotatedWithName("PlayCard1")
      .toInstance(commandImpl.PlayCardCommand(1))
    bind[ICommand]
      .annotatedWithName("PlayCard2")
      .toInstance(commandImpl.PlayCardCommand(2))
    bind[ICommand]
      .annotatedWithName("Echo")
      .toInstance(commandImpl.EchoCommand())
    bind[ICommand]
      .annotatedWithName("Start")
      .toInstance(commandImpl.StartCommand())
    bind[ICommand]
      .annotatedWithName("Pause")
      .toInstance(commandImpl.PauseCommand())
    bind[ICommand]
      .annotatedWithName("Resume")
      .toInstance(commandImpl.ResumeCommand())
    bind[ICommand]
      .annotatedWithName("PlayerSize")
      .toInstance(commandImpl.PlayerSizeCommand())
    bind[ICommand]
      .annotatedWithName("GridSize")
      .toInstance(commandImpl.GridSizeCommand())
    bind[ICommand]
      .annotatedWithName("Quit")
      .toInstance(commandImpl.QuitCommand())
    bind[ICommand]
      .annotatedWithName("Save")
      .toInstance(commandImpl.SaveGameCommand())
    bind[ICommand]
      .annotatedWithName("Load")
      .toInstance(commandImpl.LoadGameCommand())

    bind[ICommand]
      .annotatedWithName("History")
      .toInstance(commandImpl.CommandHistory())
  }
}
