package components

import subsystems.GraphicSubsystem
import org.jsfml.graphics.RenderTarget
import fanorona.rules.GameBoard
import org.jsfml.graphics.*
import org.jsfml.system.*
import fanorona.*
import fanorona.rules.*

class BoardGraphicComponent(subsystem: GraphicSubsystem, private val board: GameBoard) : GraphicComponent(subsystem) {
    private val color = Color(128, 128, 128)
    public val view: View = View(FloatRect(-1f, -1f, board.width.toFloat() + 1, board.height.toFloat() + 1))
    public var activePiece: GamePiece? = null
    public var targetTile: GameBoardTile? = null

    override fun draw(deltaTime: Float, target: RenderTarget) {
        view.setViewport(FloatRect(0.165f, 0.165f, 0.67f, 0.67f))
        target.setView(view)

        for (tile in board.tiles.values()) {
            for (direction in tile.directions filter { it.x > 0 || it.y > 0 }) {
                val rect = RectangleShape(Vector2f(0.125f, direction.length))
                rect.setOrigin(0.0625f, 0f)
                rect.setRotation(Math.atan2(direction.y.toDouble(), direction.x.toDouble()).toFloat() / Math.PI.toFloat() * 180f - 90f)
                rect.setPosition(tile.position.toVector())
                rect.setFillColor(if (targetTile != null && activePiece != null &&
                        ((tile == targetTile && activePiece!!.position - direction == tile.position) ||
                        (tile.position == activePiece!!.position && tile.position + direction == targetTile!!.position)))
                        Color.BLUE else color)
                target.draw(rect)
            }
        }

        // another failed safe cast
        if (targetTile != null) {
            val circle = CircleShape(0.455f, 8)
            circle.setFillColor(Color.BLUE)
            circle.setOrigin(0.455f, 0.455f)
            circle.setPosition(targetTile!!.position.toVector())
            target.draw(circle)
        }

        for (piece in board.tiles.values() filter { it.piece != null } map { it.piece!! }) {
            val circle = CircleShape(0.33f, 8)
            circle.setFillColor(if (piece.owner == Player.WHITE) Color.WHITE else Color.BLACK)
            circle.setOutlineColor(if (piece == activePiece) Color.BLUE else color)
            circle.setOutlineThickness(0.125f)
            circle.setOrigin(Vector2f(0.33f, 0.33f))
            circle.setPosition(piece.position.toVector())
            target.draw(circle)
        }

        target.setView(target.getDefaultView())
    }
}