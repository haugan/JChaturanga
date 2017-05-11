package engine.board;

import com.google.common.collect.ImmutableMap;
import engine.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

import static engine.board.BoardUtilities.SQUARES_ON_BOARD;

public abstract class Square {

    protected final int position; // squarePos on Board from top-left to bottom-right; 0 to 63.
    private static final Map<Integer, EmptySquare> EMPTY_SQUARE_MAP = createEmptySquareMap();

    private Square(final int position) {
        this.position = position;
    }

    public abstract boolean isOccupied();
    public abstract Piece getPiece();
    public int getPosition() {return this.position;}

    /**
     * @return initial map of all (initially empty) Squares on Board.
     */
    private static Map<Integer, EmptySquare> createEmptySquareMap() {
        final Map<Integer, EmptySquare> emptySquareMap = new HashMap<>();
        for (int i = 0; i < SQUARES_ON_BOARD; i++) {
            emptySquareMap.put(i, new EmptySquare(i));
        }
        return ImmutableMap.copyOf(emptySquareMap);
    }

    /**
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param piece that will occupy Square.
     * @return Square with attached Piece at given squarePos.
     */
    public static Square createSquare(final int position, final Piece piece) {
        return
        (piece != null) ? new OccupiedSquare(position, piece) : EMPTY_SQUARE_MAP.get(position);
    }

    // INNER CLASS!
    static final class EmptySquare extends Square {

        private EmptySquare(final int position) {
            super(position);
        }

        @Override
        public String toString() {return "-";}

        public Piece getPiece() {return null;}
        public boolean isOccupied() {return false;}
    }

    // INNER CLASS!
    static final class OccupiedSquare extends Square {

        private final Piece piece;

        private OccupiedSquare(final int position, final Piece piece) {
            super(position);
            this.piece = piece;
        }

        @Override
        public String toString() {
            return getPiece().getColor().isBlack()        // if Color of Piece is black..
                   ? getPiece().toString().toLowerCase()  // show in lower case characters
                   : getPiece().toString().toUpperCase(); // show whites in upper case characters
        }

        public Piece getPiece() {return piece;}
        public boolean isOccupied() {return true;}
    }

}