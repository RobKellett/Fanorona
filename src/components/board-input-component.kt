package components

import subsystems.UpdateSubsystem
import org.jsfml.graphics.View
import org.jsfml.graphics.RenderWindow
import org.jsfml.window.Mouse
import org.jsfml.system.Vector2f
import fanorona.rules.*
import java.util.ArrayList

class BoardInputComponent(subsystem: UpdateSubsystem, private val view: View, private val window: RenderWindow, private val board: GameBoard) : UpdateComponent(subsystem) {
    public var activePiece: GamePiece? = null
        public get private set

    public var targetTile: GameBoardTile? = null
        public get private set

    public var validActions: List<GameAction> = ArrayList()

    private var activeSelected = false
    private var wasLeftDown = false
    private var wasRightDown = false

    override fun update(deltaTime: Float) {
        val cursorPos = window.mapPixelToCoords(Mouse.getPosition(window), view)

        val posMatchesCursor = { (pos: Position) -> pos.x == (cursorPos.x + 0.5f).toInt() && pos.y == (cursorPos.y + 0.5f).toInt() }

        if (Mouse.isButtonPressed(Mouse.Button.LEFT)) {
            if (!wasLeftDown) {
                if (!activeSelected) {
                    if (activePiece != null)
                        activeSelected = true
                }
                else {
                    // TODO: Do the move!
                }
            }
            wasLeftDown = true
        }
        else
            wasLeftDown = false

        if (Mouse.isButtonPressed(Mouse.Button.RIGHT)) {
            if (!wasRightDown) {
                targetTile = null
                activeSelected = false
            }
            wasRightDown = true
        }
        else
            wasRightDown = false


        if (!activeSelected)
            activePiece = (validActions firstOrNull { posMatchesCursor(it.piece.position) })?.piece
        else
            targetTile = validActions filter { it.piece == activePiece } map { board.tiles[it.piece.position + it.action] } singleOrNull { posMatchesCursor(it.position) }
    }
}