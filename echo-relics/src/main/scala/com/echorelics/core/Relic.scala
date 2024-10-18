package com.echorelics.core

import com.echorelics.effects.Effect

final case class Relic(name: String, effect: Effect) {
  def use(player: Player): Unit = {
    effect.applyEffect(player)
  }
}
