package model.events

import model.{Echo, Player}
import utils.{Direction, Position}

enum GameEvent:
  // Start/Running Game Events
  case OnGameStartEvent
  case OnGameResumeEvent
  case OnPlayerMoveEvent
  case OnEchoSpawnEvent
  case OnRelicSpawnEvent
  case OnPlayerDeathEvent(player: Player)
  case OnMoveEchoEvent(echo: Echo)
  case OnStealRelicEvent(echo: Echo)

  // End Game Events
  case OnGameEndEvent

  // Set Game Size Events
  case OnSetGridSizeEvent(size: Int)

  // Set Player Size Events
  case OnSetPlayerSizeEvent(size: Int)

  // Pause Game Events
  case OnGamePauseEvent

  // Special
  case OnErrorEvent(message: String)
  case OnQuitEvent
  case OnNoneEvent
