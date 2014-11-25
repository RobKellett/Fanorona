package fanorona.rules

import java.util.ArrayList

enum class Player {
    WHITE
    BLACK

    public fun not(): Player = if (this == WHITE) BLACK else WHITE
}

// note: this should be an enum class, but declaring it as such
// crashes the compiler as of Kotlin 0.9.66
data class Delta(public val x: Int, public val y: Int) {
    class object {
        public val NORTH:     Delta = Delta(0, -1)
        public val NORTHEAST: Delta = Delta(1, -1)
        public val EAST:      Delta = Delta(1, 0)
        public val SOUTHEAST: Delta = Delta(1, 1)
        public val SOUTH:     Delta = Delta(0, 1)
        public val SOUTHWEST: Delta = Delta(-1, 1)
        public val WEST:      Delta = Delta(-1, 0)
        public val NORTHWEST: Delta = Delta(-1, -1);

        public val directions: Array<Delta> = array(NORTH, NORTHEAST, EAST, SOUTHEAST,
                                                    SOUTH, SOUTHWEST, WEST, NORTHWEST)
    }

    public fun plus(other: Delta): Delta = Delta(x + other.x, y + other.y)
    public fun plus(other: Position): Position = Position(x + other.x, y + other.y)
    public fun minus(): Delta = Delta(-x, -y)
    public fun minus(other: Delta): Delta = this + -other
}

data class Position(public val x: Int, public val y: Int) {
    public fun plus(other: Delta):  Position = this + other
    public fun minus(other: Delta): Position = this + -other
}

data class GamePiece(public val owner: Player, public var position: Position)

data class GameAction(public val piece: GamePiece, public val action: Delta,
                      public val casualties: List<GamePiece>)

class GameBoard(private val width: Int, private val height: Int) {
    private val pieces = ArrayList<GamePiece>();

    // constructor
    {
        assert(width % 2 == 1, "Width must be odd.")
        assert(height % 2 == 1, "Height must be odd.")

        for (y in 0..height) {
            for (x in 0..width) {
                // center piece is blank
                if (y == height / 2 && x == width / 2)
                    continue;

                val player =
                        if (y < height / 2)
                            Player.BLACK
                        else if (y > height / 2)
                            Player.WHITE
                        else if (x < width / 2)
                            if (x % 2 == 0) Player.BLACK else Player.WHITE
                        else
                            if (x % 2 == 0) Player.WHITE else Player.BLACK

                pieces.add(GamePiece(player, Position(x, y)))
            }
        }
    }

    private fun inBounds(position: Position) = position.x >= 0 && position.x < width &&
                                               position.y >= 0 && position.y < height

    public fun performAction(action: GameAction) {
        var piece = pieces.single { it == action.piece }
        piece.position += action.action
        pieces.removeAll(pieces.filter { it in action.casualties })
    }

    public fun getActionsForPlayer(player: Player): List<GameAction> =
            getActionsForPieces(pieces.filter { it.owner == player })

    public fun getActionsForPiece(piece: GamePiece): List<GameAction> =
            getActionsForPieces(listOf(piece))

    private fun getActionsForPieces(pieces: Collection<GamePiece>): List<GameAction> {
        val result = ArrayList<GameAction>()
        for (piece in pieces) {
            for (direction in Delta.directions) {
                val destination = piece.position + direction

                // skip out-of-bounds destinations
                if (!inBounds(destination))
                    continue;

                // skip occupied destinations
                if (pieces.any { it.position == destination })
                    continue;

                // forward capture
                val forwardCapturePieces = ArrayList<GamePiece>()
                var forwardLookup = destination + direction
                while (inBounds(forwardLookup)) {
                    val forwardPiece = pieces.singleOrNull { it.position == forwardLookup }

                    if (forwardPiece?.owner != !piece.owner)
                        break;

                    forwardCapturePieces.add(forwardPiece)
                    forwardLookup += direction
                }
                if (forwardCapturePieces.notEmpty)
                    result.add(GameAction(piece, direction, forwardCapturePieces))

                // backward capture
                val backwardCapturePieces = ArrayList<GamePiece>()
                var backwardLookup = piece.position - direction
                while (inBounds(backwardLookup)) {
                    val backwardPiece = pieces.singleOrNull { it.position == backwardLookup }

                    if (backwardPiece?.owner != !piece.owner)
                        break;

                    backwardCapturePieces.add(backwardPiece)
                    backwardLookup -= direction
                }
                if (backwardCapturePieces.notEmpty)
                    result.add(GameAction(piece, direction, backwardCapturePieces))

                // just a move
                if (forwardCapturePieces.empty && backwardCapturePieces.empty)
                    result.add(GameAction(piece, direction, ArrayList<GamePiece>()))
            }
        }

        return result;
    }
}