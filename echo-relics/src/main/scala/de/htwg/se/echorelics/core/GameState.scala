package de.htwg.se.echorelics.core

sealed trait GameState
object GameState {
  case object Running extends GameState
  case object GameOver extends GameState
  case object Paused extends GameState
  case object Victory extends GameState
}
