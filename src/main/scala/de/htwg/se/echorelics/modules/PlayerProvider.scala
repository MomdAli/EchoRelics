package modules

import com.google.inject.Provider

import model.entity.entityImpl.Player

class PlayerProvider extends Provider[Player] {
  var id: String = ""

  def setId(newId: String): Unit = {
    id = newId
  }

  override def get(): Player = {
    new Player(id)
  }
}
