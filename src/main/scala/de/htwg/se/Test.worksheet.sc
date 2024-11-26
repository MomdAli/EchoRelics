import model.generator.Spawner
import model.{Grid, Tile, TileContent}

val w = Spawner.generateWalls(new Grid(20), 0x3341, 4)
val a = Spawner.spawnRelics(w, 13144, 20)
