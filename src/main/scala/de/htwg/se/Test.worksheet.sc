import model.{Grid, Tile}
import model.entity.{Player, Wall, Relic}
import utils.Position

val grid = new Grid(10)

val position = Position(2, 3)

val test = grid.tileAt(position).entity.isEmpty
