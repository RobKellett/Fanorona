package states

import org.jsfml.graphics.RenderTarget
import subsystems.*
import fanorona.rules.*
import utilities.drawTextCentered
import components.BoardGraphicComponent
import components.BoardInputComponent
import org.jsfml.graphics.RenderWindow
import org.jsfml.system.Vector2i
import fanorona.screenWidth
import org.jsfml.graphics.Color
import org.jsfml.window.Keyboard
import fanorona.changeState

enum class PlayerType {
    HUMAN
    MACHINE
}

class GameplayState(boardWidth: Int, boardHeight: Int, whitePlayer: PlayerType, blackPlayer: PlayerType, private val window: RenderWindow) : GameState() {
    private val players = mapOf(Pair(Player.WHITE, whitePlayer), Pair(Player.BLACK, blackPlayer))
    private var turn = Player.WHITE
    private var gameOver = false

    // This is the model to internally represent our game logic
    private val gameBoard = GameBoard(boardWidth, boardHeight)

    public val chooseAction: (GameAction?) -> Unit = {
        if (it != null) {
            gameBoard.performAction(it)
            turn = !turn
            validActions = gameBoard.getActionsForPlayer(turn)
        }

        if (it == null || validActions.empty) {
            turn = !turn // back to the winner
            gameOver = true
        }
    }

    // These are the views to represent the game to the player
    private val gameBoardGraphics = BoardGraphicComponent(graphicSubsystem, gameBoard)
    private val gameBoardInput = BoardInputComponent(updateSubsystem, gameBoardGraphics.view, window, gameBoard, chooseAction)

    private var validActions = gameBoard.getActionsForPlayer(Player.WHITE)

    override fun update(deltaTime: Float) {
        gameBoardInput.active = players[turn] == PlayerType.HUMAN && !gameOver
        gameBoardGraphics.inputActive = gameBoardInput.active
        gameBoardInput.validActions = validActions

        super.update(deltaTime)

        gameBoardGraphics.activePiece = gameBoardInput.activePiece
        gameBoardGraphics.targetActions = gameBoardInput.targetActions
        gameBoardGraphics.turn = turn
    }

    override fun draw(deltaTime: Float, target: RenderTarget) {
        // this is a very poor place to put this
        if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE))
            changeState(MainMenuState(window, target.getDefaultView()))

        super.draw(deltaTime, target)

        drawTextCentered("${if (turn == Player.WHITE) "White" else "Black"}${if (gameOver) " wins!" else "'s turn"}",
                Vector2i(screenWidth / 2, 25), if (turn == Player.WHITE) Color.BLACK else Color.WHITE, target)
    }
}