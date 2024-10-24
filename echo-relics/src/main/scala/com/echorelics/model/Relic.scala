package com.echorelics.model

import com.echorelics.effects.Effect

final case class Relic(name: String, effect: Effect) {
  def use(player: Player): Unit = {
    effect.applyEffect(player)
  }
}

object Relic {
  def defaultRelic(): Relic = {
    Relic("Default Relic", Effect.defaultEffect())
  }
}
