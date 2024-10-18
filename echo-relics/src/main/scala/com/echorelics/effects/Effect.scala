package com.echorelics.effects

import com.echorelics.core.Player

trait Effect {
  def applyEffect(player: Player): Unit
}
