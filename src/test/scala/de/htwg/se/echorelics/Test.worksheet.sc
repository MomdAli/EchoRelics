import utils.DisplayRenderer
import model.Grid
import model.Tile
import model.TileContent
val t = DisplayRenderer.render(
  new Grid(
    Vector(
      Vector(
        Tile(TileContent.Empty),
        Tile(TileContent.Empty),
        Tile(TileContent.Empty)
      ),
      Vector(
        Tile(TileContent.Empty),
        Tile(TileContent.Empty),
        Tile(TileContent.Empty)
      ),
      Vector(
        Tile(TileContent.Empty),
        Tile(TileContent.Empty),
        Tile(TileContent.Empty)
      )
    )
  )
)

print(t)
