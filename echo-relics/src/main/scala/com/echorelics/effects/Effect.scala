package com.echorelics.effects

import com.echorelics.model.Player

trait Effect {
  def applyEffect(player: Player): Unit
}

object Effect {
  def defaultEffect(): Effect = new Effect {
    override def applyEffect(player: Player): Unit = {
      println(s"Default effect applied to player ${player.id}")
    }
  }
}
