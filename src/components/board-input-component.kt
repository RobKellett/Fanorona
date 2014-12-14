package components

import subsystems.UpdateSubsystem
import org.jsfml.graphics.View
import org.jsfml.graphics.RenderWindow
import org.jsfml.window.Mouse
import org.jsfml.system.Vector2f
import fanorona.rules.*
import java.util.ArrayList
import org.jsfml.window.Keyboard

class BoardInputComponent(subsystem: UpdateSubsystem, private val view: View, private val window: RenderWindow, private val board: GameBoard, private val actionCallback: (GameAction?) -> Unit) : UpdateComponent(subsystem) {
    public var activePiece: GamePiece? = null
        public get private set

    public var targetActions: ArrayList<GameAction> = ArrayList()
        public get private set

    public var active: Boolean = false;

    public var validActions: List<GameAction> = ArrayList()

    private var activeSelected = false
    private var wasLeftDown = false
    private var wasRightDown = false

    override fun update(deltaTime: Float) {
        if (!active)
            return;

        val cursorPos = window.mapPixelToCoords(Mouse.getPosition(window), view)

        val posMatchesCursor = { (pos: Position) -> pos.x == (cursorPos.x + 0.5f).toInt() && pos.y == (cursorPos.y + 0.5f).toInt() }

        if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE))
            actionCallback(null) // forfeit

        if (Mouse.isButtonPressed(Mouse.Button.LEFT)) {
            if (!wasLeftDown) {
                if (!activeSelected) {
                    if (activePiece != null)
                        activeSelected = true
                }
                else if (targetActions.notEmpty) {
                    // these "smart" casts are killing me
                    actionCallback(if (Keyboard.isKeyPressed(Keyboard.Key.LSHIFT)) targetActions.last() else targetActions.first())
                    activeSelected = false
                    activePiece = null
                    targetActions.clear()
                }
            }
            wasLeftDown = true
        }
        else
            wasLeftDown = false

        if (Mouse.isButtonPressed(Mouse.Button.RIGHT)) {
            if (!wasRightDown) {
                targetActions.clear()
                activeSelected = false
            }
            wasRightDown = true
        }
        else
            wasRightDown = false


        if (!activeSelected)
            activePiece = (validActions firstOrNull { posMatchesCursor(it.piece.position) })?.piece
        else
            targetActions = (validActions filter { it.piece == activePiece && posMatchesCursor(it.piece.position + it.action) }).toArrayList()
    }
}