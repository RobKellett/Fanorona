package states

import org.jsfml.graphics.RenderTarget
import subsystems.*
import fanorona.rules.*
import components.BoardGraphicComponent
import components.BoardInputComponent
import org.jsfml.graphics.RenderWindow

class GameplayState(boardWidth: Int, boardHeight: Int, window: RenderWindow) : GameState() {
    // This is the model to internally represent our game logic
    private val gameBoard = GameBoard(boardWidth, boardHeight)

    // These are the views to represent the game to the player
    private val gameBoardGraphics = BoardGraphicComponent(graphicSubsystem, gameBoard)
    private val gameBoardInput = BoardInputComponent(updateSubsystem, gameBoardGraphics.view, window, gameBoard)

    private var validActions = gameBoard.getActionsForPlayer(Player.WHITE)

    override fun update(deltaTime: Float) {
        gameBoardInput.validActions = validActions

        super.update(deltaTime)

        gameBoardGraphics.activePiece = gameBoardInput.activePiece
        gameBoardGraphics.targetTile = gameBoardInput.targetTile
    }

    override fun draw(deltaTime: Float, target: RenderTarget) {
        super.draw(deltaTime, target)
    }
}