package model.events

import model.entity.{Echo, Player, Relic}
import utils.{Direction, Position}
import model.item.Card

enum GameEvent:
  // Start/Running Game Events
  case OnGameStartEvent
  case OnGameResumeEvent
  // Players
  case OnPlayerMoveEvent
  case OnPlayerDeathEvent(player: Player)
  case OnPlayCardEvent(card: Card)
  // Echoes
  case OnEchoSpawnEvent
  case OnMoveEchoEvent(echo: Echo)
  // Relics
  case OnRelicSpawnEvent
  case OnRelicCollectEvent(player: Player, relic: Relic)
  // Cards
  case OnTimeTravelEvent(turns: Int)

  // End Game Events
  case OnGameEndEvent

  // Set Game Size Events
  case OnSetGridSizeEvent(size: Int)

  // Set Player Size Events
  case OnSetPlayerSizeEvent(size: Int)

  // Pause Game Events
  case OnGamePauseEvent

  // Special
  case OnUpdateRenderEvent
  case OnErrorEvent(message: String)
  case OnInfoEvent(message: String)
  case OnQuitEvent
  case OnNoneEvent
