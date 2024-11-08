package de.htwg.se.echorelics

import de.htwg.se.echorelics.core.GameManager

@main def echorelics(): Unit = {
  val gameManager = GameManager()
  gameManager.run()
}
