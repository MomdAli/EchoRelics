package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import utils.Direction

class GameManagerSpec extends AnyWordSpec with Matchers {

    "A GameManager" should {

        "have a default state of NotStarted" in {
            val gameManager = GameManager()
            gameManager.getState should be(GameState.NotStarted)
        }

        "add a player when the game is not started" in {
            val gameManager = GameManager()
            val player = Player("1")
            val updatedGameManager = gameManager.addPlayer(player)
            updatedGameManager.players should contain(player)
        }

        "not add a player when the game is running" in {
            val gameManager = GameManager().startGame()
            val player = Player("1")
            val updatedGameManager = gameManager.addPlayer(player)
            updatedGameManager.players should not contain player
        }

        "remove a player when the game is not started" in {
            val player = Player("1")
            val gameManager = GameManager(players = List(player))
            val updatedGameManager = gameManager.removePlayer(player)
            updatedGameManager.players should not contain player
        }

        "not remove a player when the game is running" in {
            val player = Player("1")
            val gameManager = GameManager(players = List(player)).startGame()
            val updatedGameManager = gameManager.removePlayer(player)
            updatedGameManager.players should contain(player)
        }

        "start the game with players" in {
            val player = Player("1")
            val gameManager = GameManager(players = List(player))
            val updatedGameManager = gameManager.startGame()
            updatedGameManager.getState should be(GameState.Running)
        }

        "start the game with default players if no players are added" in {
            val gameManager = GameManager()
            val updatedGameManager = gameManager.startGame()
            updatedGameManager.players should have size 2
            updatedGameManager.getState should be(GameState.Running)
        }

        "end the game" in {
            val gameManager = GameManager().startGame()
            val updatedGameManager = gameManager.endGame()
            updatedGameManager.getState should be(GameState.GameOver)
        }

        "pause the game" in {
            val gameManager = GameManager().startGame()
            val updatedGameManager = gameManager.pauseGame()
            updatedGameManager.getState should be(GameState.Paused)
        }

        "resume the game" in {
            val gameManager = GameManager().startGame().pauseGame()
            val updatedGameManager = gameManager.resumeGame()
            updatedGameManager.getState should be(GameState.Running)
        }

        "move the current player" in {
            val player = Player("1")
            val gameManager = GameManager(players = List(player)).startGame()
            val updatedGameManager = gameManager.moveNextPlayer(Direction.Up)
            updatedGameManager.move should be(1)
        }

        "not move the player if the game is not running" in {
            val player = Player("1")
            val gameManager = GameManager(players = List(player))
            val updatedGameManager = gameManager.moveNextPlayer(Direction.Up)
            updatedGameManager.move should be(0)
        }

        "display the grid when the game is running" in {
            val player = Player("1")
            val gameManager = GameManager(players = List(player)).startGame()
            gameManager.displayGrid should not be empty
        }

        "not display the grid when the game is not started" in {
            val gameManager = GameManager()
            gameManager.displayGrid should be("")
        }

        "provide game info" in {
            val player = Player("1")
            val gameManager = GameManager(players = List(player)).startGame()
            gameManager.getInfo should include("State: Running")
            gameManager.getInfo should include("1's turn")
        }
    }
}