package fanorona.rules

import java.util.ArrayList
import java.util.HashMap
import java.util.Dictionary
import org.jsfml.system.Vector2f

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

    public val lengthSquared: Int get() = x * x + y * y
    public val length: Float get() = Math.sqrt(lengthSquared.toDouble()).toFloat()
}

data class Position(public val x: Int, public val y: Int) {
    public fun plus(other: Delta):  Position =  other + this
    public fun minus(other: Delta): Position = -other + this
    public fun toVector(): Vector2f = Vector2f(x.toFloat(), y.toFloat())
}

data class GamePiece(public val owner: Player, public var position: Position)

class GameBoardTile(public val position: Position, public var directions: Array<Delta>, public var piece: GamePiece? = null)

data class GameAction(public val piece: GamePiece, public val action: Delta,
                      public val casualties: List<GamePiece>)

class GameBoard(public val width: Int, public val height: Int) {
    //private val pieces = ArrayList<GamePiece>();
    public val tiles: HashMap<Position, GameBoardTile> = HashMap();

    // constructor
    {
        assert(width % 2 == 1, "Width must be odd.")
        assert(height % 2 == 1, "Height must be odd.")

        for (y in 0..height-1) {
            for (x in 0..width-1) {
                val player =
                        if (y == height / 2 && x == width / 2)
                            null
                        else if (y < height / 2)
                            Player.BLACK
                        else if (y > height / 2)
                            Player.WHITE
                        else if (x < width / 2)
                            if (x % 2 == 0) Player.BLACK else Player.WHITE
                        else
                            if (x % 2 == 0) Player.WHITE else Player.BLACK

                val directions = arrayListOf(Delta.NORTH, Delta.EAST, Delta.SOUTH, Delta.WEST)
                if (x % 2 == y % 2)
                    directions.addAll(array(Delta.NORTHEAST, Delta.SOUTHEAST, Delta.SOUTHWEST, Delta.NORTHWEST))

                // remove out-of-bounds moves
                if (x == 0)
                    directions.removeAll(array(Delta.NORTHWEST, Delta.WEST, Delta.SOUTHWEST))
                else if (x == width - 1)
                    directions.removeAll(array(Delta.NORTHEAST, Delta.EAST, Delta.SOUTHEAST))

                if (y == 0)
                    directions.removeAll(array(Delta.NORTHWEST, Delta.NORTH, Delta.NORTHEAST))
                else if (y == height - 1)
                    directions.removeAll(array(Delta.SOUTHWEST, Delta.SOUTH, Delta.SOUTHEAST))

                tiles[Position(x, y)] = GameBoardTile(Position(x, y), directions.copyToArray(),
                        if (player == null) null else GamePiece(player, Position(x, y)))
            }
        }
    }

    private fun inBounds(position: Position) = position.x >= 0 && position.x < width &&
                                               position.y >= 0 && position.y < height

    public fun performAction(action: GameAction) {
        val destination = action.piece.position + action.action
        tiles[destination].piece = action.piece
        tiles[action.piece.position].piece = null
        action.piece.position = destination
        for (deceased in action.casualties)
            tiles[deceased.position].piece = null
    }

    public fun getActionsForPlayer(player: Player): List<GameAction> =
            getActionsForTiles(tiles.values() filter { it.piece != null && it.piece!!.owner == player })

    public fun getActionsForTile(tile: GameBoardTile): List<GameAction> =
            getActionsForTiles(listOf(tile))

    private fun getActionsForTiles(actionTiles: Collection<GameBoardTile>): List<GameAction> {
        val result = ArrayList<GameAction>()
        for (tile in actionTiles) {
            // This should smart cast, but doesn't
            if (tile.piece == null)
                continue;

            val piece = tile.piece!!;

            for (direction in tile.directions) {
                val destination = tile.position + direction

                // skip occupied destinations
                if (tiles.values().any { it.position == destination && it.piece != null })
                    continue;

                // approach capture
                val forwardCapturePieces = ArrayList<GamePiece>()
                var forwardLookup = destination + direction
                while (inBounds(forwardLookup)) {
                    val forwardPiece = actionTiles.singleOrNull { it.position == forwardLookup && it.piece != null }?.piece

                    if (forwardPiece?.owner != !piece.owner)
                        break;

                    forwardCapturePieces.add(forwardPiece)
                    forwardLookup += direction
                }
                if (forwardCapturePieces.notEmpty)
                    result.add(GameAction(piece, direction, forwardCapturePieces))

                // withdrawal capture
                val backwardCapturePieces = ArrayList<GamePiece>()
                var backwardLookup = piece.position - direction
                while (inBounds(backwardLookup)) {
                    val backwardPiece = actionTiles.singleOrNull { it.position == backwardLookup && it.piece != null }?.piece

                    if (backwardPiece?.owner != !piece.owner)
                        break;

                    backwardCapturePieces.add(backwardPiece)
                    backwardLookup -= direction
                }
                if (backwardCapturePieces.notEmpty)
                    result.add(GameAction(piece, direction, backwardCapturePieces))

                // just a paiko move
                if (forwardCapturePieces.empty && backwardCapturePieces.empty)
                    result.add(GameAction(piece, direction, ArrayList<GamePiece>()))
            }
        }

        return result;
    }
}