package model.events

import model.entity.IEntity
import utils.{Direction, Position}
import model.item.ICard

enum GameEvent:
  // Start/Running Game Events
  case OnGameStartEvent
  case OnGameResumeEvent
  // Players
  case OnPlayerMoveEvent
  case OnPlayerDeathEvent(player: IEntity)
  case OnPlayCardEvent(card: ICard)
  case OnPlayerDamageEvent(player: IEntity)
  // Echoes
  case OnEchoSpawnEvent
  case OnEchoesMoveEvent
  case OnEchoMoveEvent(echo: IEntity)
  // Relics
  case OnRelicSpawnEvent
  case OnRelicCollectEvent(player: IEntity, relic: IEntity)
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

  // Winner
  case OnWinnerEvent(player: String)

  // Special
  case OnUpdateRenderEvent
  case OnErrorEvent(message: String)
  case OnInfoEvent(message: String)
  case OnQuitEvent
  case OnNoneEvent
  case OnLoadSaveEvent
