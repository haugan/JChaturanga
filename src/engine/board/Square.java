package engine.board;

import com.google.common.collect.ImmutableMap;
import engine.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

import static engine.board.BoardUtilities.SQUARES_ON_BOARD;

public abstract class Square {

    protected final int position; // position of Square on Board
    private static final Map<Integer, EmptySquare> EMPTY_SQUARE_MAP = createEmptySquareMap();

    /**
     * @param position of Square from top-left to bottom-right; 0 to 63.
     */
    private Square(final int position) {
        this.position = position;
    }

    /**
     * @return map of all Squares on Board.
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
     * @return Square with attached Piece at given position.
     */
    public static Square createSquare(final int position, final Piece piece) {
        return piece != null                         // if Piece is not null..
               ? new OccupiedSquare(position, piece) // return new Square with attached Piece
               : EMPTY_SQUARE_MAP.get(position);     // return empty Square
    }

    public abstract boolean isOccupied();
    public abstract Piece getPiece();

    // INNER CLASS!
    static final class EmptySquare extends Square {

        /**
         * @param position of Square from top-left to bottom-right; 0 to 63.
         */
        private EmptySquare(final int position) {
            super(position);
        }

        @Override
        public boolean isOccupied() {return false;}

        @Override
        public Piece getPiece() {return null;}

        @Override
        public String toString() {
            return "-";
        }
    }

    // INNER CLASS!
    static final class OccupiedSquare extends Square {

        private final Piece piece; // chess Piece occupying this Square

        /**
         * @param position of Square from top-left to bottom-right; 0 to 63.
         * @param piece that occupies Square.
         */
        private OccupiedSquare(final int position, final Piece piece) {
            super(position);
            this.piece = piece;
        }

        @Override
        public boolean isOccupied() {return true;}

        @Override
        public Piece getPiece() {return piece;}

        @Override
        public String toString() {
            return getPiece().getColor().isBlack()        // if Color of Piece is black..
                   ? getPiece().toString().toLowerCase()  // show in lower case characters
                   : getPiece().toString().toUpperCase(); // show whites in upper case characters
        }
    }

}