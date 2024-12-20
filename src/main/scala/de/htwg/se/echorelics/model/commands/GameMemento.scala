package model.commands

import model.entity.Player
import model.{Grid, GameState}

/** A case class representing a memento for the game state.
  *
  * @param players
  *   A list of players in the game.
  * @param grid
  *   The current state of the game grid.
  */
case class GameMemento(
    grid: Grid,
    state: GameState
)
