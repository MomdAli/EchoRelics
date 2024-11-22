import model.Grid
import config.Config
import services.generator.WallGenerator

WallGenerator.generateWalls(new Grid(5), 9183389L, Config(wallRatio = 2))