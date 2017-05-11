package engine.pieces;

import engine.board.Board;
import engine.moves.Move;
import engine.players.PlayerColor;

import java.util.Collection;

public abstract class Piece {

    protected final int pos; // numbered position of Piece on Board (Squares top-left to bottom-right; 0-63)
    protected final PlayerColor color;
    private final PieceType type;
    protected final boolean firstMove;
    private final int hashCode; // keeps cached value (Piece is immutable, needs only calc. hash code once)

    public Piece(final int pos, final PlayerColor color, final PieceType type, final boolean firstMove) {
        this.pos = pos;
        this.color = color;
        this.type = type;
        this.firstMove = firstMove;
        hashCode = createHashCode();
    }

    /**
     * When adding an object to a hash table, it gets a key value used to store and retrieve it.
     * The key's hash code is calculated, and determines "an area" in memory to search for an object that is stored.
     * Hash codes aren't unique, and several keys might share the same ones.
     * The hash table thus iterates "the area", and uses the key's equals() method to find the right key,
     * in order to retrieve the correct object stored with that key.
     *
     * @return hash code of key that is created, stored, and used for retrieving an object from a hash table.
     */
    @Override
    public int hashCode() {return hashCode;}

    /**
     * Overridden from JRE to test for object equality over default reference-only equality test.
     * A Piece object copy might have changed from the original (e.g. new pos on Board).
     * (ref. http://tutorials.jenkov.com/java-collections/hashcode-equals.html)
     * @param obj that is tested for equality.
     * @return true (if the two objects has equal "states", i.e. same variable values, etc.)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Piece)) {
            return false;
        }

        final Piece other = (Piece) obj;

        return pos == other.pos &&
               color == other.color &&
               type == other.type &&
               firstMove == other.firstMove;
    }

    /**
     * @return a unique integer value for each particular (and immutable) Piece.
     */
    private int createHashCode() {
        int hashCode = type.hashCode();
        hashCode = 31 * hashCode + color.hashCode();
        hashCode = 31 * hashCode + pos;
        hashCode = 31 * hashCode + (isFirstMove() ? 1 : 0);
        return hashCode;
    }

    public int getPos() {return pos;}
    public PlayerColor getColor() {return color;}
    public PieceType getType() {return type;}
    public boolean isFirstMove() {return firstMove;}

    /**
     * The Piece class is immutable (ref. Joshua Block - Effective Java), following best practices.
     * Thus, this method creates a new Piece object with updated positioning from Move object that's passed to it.
     * @param move object containing new pos for Piece, and color of "moving" Player.
     * @return new Piece with updated pos.
     */
    public abstract Piece performMove(final Move move);
    public abstract Collection<Move> calculateLegalMoves(final Board board);


    // INNER CLASS!
    public enum PieceType {

        KING("k"), QUEEN("q"), ROOK("r"), KNIGHT("n"), BISHOP("b"), PAWN("p");

        private String type;

        PieceType(final String type) {
            this.type = type;
        }

        @Override
        public String toString() {return type;}
    }

}