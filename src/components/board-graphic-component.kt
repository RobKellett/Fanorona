package components

import subsystems.GraphicSubsystem
import org.jsfml.graphics.RenderTarget
import fanorona.rules.GameBoard
import org.jsfml.graphics.*
import org.jsfml.system.*
import fanorona.*
import fanorona.rules.*
import java.util.ArrayList

class BoardGraphicComponent(subsystem: GraphicSubsystem, private val board: GameBoard) : GraphicComponent(subsystem) {
    private val color = Color(128, 128, 128)
    public val view: View = View(FloatRect(-3f, -3f, board.width.toFloat() + 5, board.height.toFloat() + 5))
    public var activePiece: GamePiece? = null
    public var targetActions: List<GameAction> = ArrayList()
    public var turn: Player = Player.WHITE;
    public var inputActive: Boolean = false;

    override fun draw(deltaTime: Float, target: RenderTarget) {
        target.setView(view)

        target.clear(if (turn == Player.WHITE) Color.WHITE else Color.BLACK)

        for (tile in board.tiles.values()) {
            for (direction in tile.directions filter { it.x > 0 || it.y > 0 }) {
                val rect = RectangleShape(Vector2f(0.125f, direction.length))
                rect.setOrigin(0.0625f, 0f)
                rect.setRotation(Math.atan2(direction.y.toDouble(), direction.x.toDouble()).toFloat() / Math.PI.toFloat() * 180f - 90f)
                rect.setPosition(tile.position.toVector())
                val targetPosition = if (targetActions.notEmpty) targetActions.first().piece.position + targetActions.first().action else null
                rect.setFillColor(if (inputActive == true && targetPosition != null && activePiece != null &&
                        ((tile.position == targetPosition && activePiece!!.position - direction == tile.position) ||
                        (tile.position == activePiece!!.position && tile.position + direction == targetPosition)))
                        Color.BLUE else color)
                target.draw(rect)
            }
        }

        // another failed safe cast
        if (inputActive && targetActions.notEmpty) {
            val circle = CircleShape(0.455f, 8)
            circle.setFillColor(Color.BLUE)
            circle.setOrigin(0.455f, 0.455f)
            val action = targetActions.first()
            circle.setPosition((action.piece.position + action.action).toVector())
            target.draw(circle)
        }

        for (piece in board.tiles.values() filter { it.piece != null } map { it.piece!! }) {
            val circle = CircleShape(0.33f, 8)
            circle.setFillColor(if (piece.owner == Player.WHITE) Color.WHITE else Color.BLACK)
            circle.setOutlineColor(when {
                inputActive && piece == activePiece -> Color.BLUE
                targetActions.notEmpty && piece in targetActions.first().casualties -> Color.RED
                targetActions.count() > 1 && piece in targetActions.last().casualties -> Color.YELLOW
                else -> color
            })
            circle.setOutlineThickness(0.125f)
            circle.setOrigin(Vector2f(0.33f, 0.33f))
            circle.setPosition(piece.position.toVector())
            target.draw(circle)
        }

        target.setView(target.getDefaultView())
    }
}