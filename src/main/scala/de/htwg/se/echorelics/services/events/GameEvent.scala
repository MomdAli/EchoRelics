package services.events

import model.{Echo, Player}
import utils.{Direction, Position}

sealed trait GameEvent

case class OnGameStartEvent() extends GameEvent

case class OnGameEndEvent() extends GameEvent

case class OnGamePauseEvent() extends GameEvent

case class OnGameResumeEvent() extends GameEvent

case class OnAddPlayerEvent(player: Player) extends GameEvent

case class OnRemovePlayerEvent(player: Player) extends GameEvent

case class OnPlayerMoveEvent(direction: Direction) extends GameEvent

case class OnSpawnEchoEvent() extends GameEvent

case class OnSetGridSizeEvent(size: Int) extends GameEvent
