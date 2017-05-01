package engine.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import engine.moves.Move;
import engine.pieces.*;
import engine.players.BlackPlayer;
import engine.players.Player;
import engine.players.PlayerColor;
import engine.players.WhitePlayer;

import java.util.*;

import static engine.board.BoardUtilities.SQUARES_ON_BOARD;
import static engine.board.BoardUtilities.SQUARES_ON_ROW;
import static engine.players.PlayerColor.*;
import static java.util.Collections.unmodifiableList;

public class Board {

    private final List<Square> squareList;
    private final Collection<Piece> blackPieces;
    private final Collection<Piece> whitePieces;
    private final Collection<Move> blackLegalMoves;
    private final Collection<Move> whiteLegalMoves;
    private final BlackPlayer blackPlayer;
    private final WhitePlayer whitePlayer;
    private final Player currentPlayer;

    /**
     * @param builder TODO: comment this
     */
    private Board(final BoardBuilder builder) {
        this.squareList = createSquareList(builder);
        this.blackPieces = getIngamePieces(this.squareList, BLACK);
        this.whitePieces = getIngamePieces(this.squareList, WHITE);
        this.blackLegalMoves = getLegalMoves(this.blackPieces);
        this.whiteLegalMoves = getLegalMoves(this.whitePieces);
        this.blackPlayer = new BlackPlayer(this, blackLegalMoves, whiteLegalMoves);
        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.currentPlayer = builder.nextToMove.setPlayer(this.whitePlayer, this.blackPlayer);
    }

    /**
     * Get Squares from list, call each overridden toString method for displaying Console graphics.
     * @return formatted ASCII graphics of chess board.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (int pos = 0; pos < SQUARES_ON_BOARD; pos++) { // loop through each position on Board
            final String square = this.squareList.get(pos).toString(); // overridden in Square class
            sb.append(String.format("%3s", square));
            if ((pos + 1) % SQUARES_ON_ROW == 0) {
                sb.append("\n"); // new line each 8th Square
            }
        }

        return sb.toString();
    }

    /**
     * Create the initial chess board positioning.
     * @return an object containing Pieces of each PlayerColor, attached to Squares.
     */
    public static Board initializeBoard() {
        final BoardBuilder b = new BoardBuilder();

        // initial BLACK positions
        b.setPiece(new Rook(0, BLACK));
        b.setPiece(new Knight(1, BLACK));
        b.setPiece(new Bishop(2, BLACK));
        b.setPiece(new Queen(3, BLACK));
        b.setPiece(new King(4, BLACK));
        b.setPiece(new Bishop(5, BLACK));
        b.setPiece(new Knight(6, BLACK));
        b.setPiece(new Rook(7, BLACK));
        b.setPiece(new Pawn(8, BLACK));
        b.setPiece(new Pawn(9, BLACK));
        b.setPiece(new Pawn(10, BLACK));
        b.setPiece(new Pawn(11, BLACK));
        b.setPiece(new Pawn(12, BLACK));
        b.setPiece(new Pawn(13, BLACK));
        b.setPiece(new Pawn(14, BLACK));
        b.setPiece(new Pawn(15, BLACK));

        // initial WHITE positions
        b.setPiece(new Pawn(48, WHITE));
        b.setPiece(new Pawn(49, WHITE));
        b.setPiece(new Pawn(50, WHITE));
        b.setPiece(new Pawn(51, WHITE));
        b.setPiece(new Pawn(52, WHITE));
        b.setPiece(new Pawn(53, WHITE));
        b.setPiece(new Pawn(54, WHITE));
        b.setPiece(new Pawn(55, WHITE));
        b.setPiece(new Rook(56, WHITE));
        b.setPiece(new Knight(57, WHITE));
        b.setPiece(new Bishop(58, WHITE));
        b.setPiece(new King(59, WHITE));
        b.setPiece(new Queen(60, WHITE));
        b.setPiece(new Bishop(61, WHITE));
        b.setPiece(new Knight(62, WHITE));
        b.setPiece(new Rook(63, WHITE));

        b.setNextToMove(WHITE);
        return b.createBoard();
    }

    /**
     * Loop through each position on Board (0-63) and create Squares (both empty & occupied).
     *
     * @param builder TODO: comment this
     * @return list of 64 Squares representing the tiles on a chess board.
     */
    private static List<Square> createSquareList(final BoardBuilder builder) {
        final Square[] squares = new Square[SQUARES_ON_BOARD];

        for (int pos = 0; pos < SQUARES_ON_BOARD; pos++) { // loop through each position on Board
            squares[pos] = Square.createSquare(
                    pos, builder.squarePieceMap.get(pos)
            ); // get Piece associated with position, and create Square (with occupying Piece)
        }

        //return unmodifiableList(Arrays.asList(squares));
        return ImmutableList.copyOf(squares); // Google Guava library
    }

    /**
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @return Square at any given position, from the list of Squares.
     */
    public Square getSquare(final int position) {return squareList.get(position);}

    /**
     * @param squares from the list of Squares, representing the chess board tiles.
     * @param color of player's Pieces, either black or white.
     * @return list of active "in-game" Pieces for each Player on the Board.
     */
    public static Collection<Piece> getIngamePieces(final List<Square> squares, final PlayerColor color) {
        final List<Piece> pieces = new ArrayList<>();

        for (final Square s : squares) {
            if (s.isOccupied()) {
                final Piece occupyingPiece = s.getPiece();
                if (occupyingPiece.getColor() == color) {
                    pieces.add(occupyingPiece);
                }
            }
        }

        return unmodifiableList(pieces);
    }

    /**
     * @param pieces which can return a collection of legal moves to the list.
     * @return list of legal moves.
     */
    public Collection<Move> getLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final Piece p : pieces) {
            legalMoves.addAll(p.calculateLegalMoves(this));
        }

        return unmodifiableList(legalMoves);
    }

    /**
     * TODO: comment this
     * @return TODO: comment this
     */
    public Iterable<Move> getBothPlayersLegalMoves() {
        return Iterables.unmodifiableIterable(
                Iterables.concat(
                        this.whitePlayer.getLegalMoves(),
                        this.blackPlayer.getLegalMoves()
                )
        ); // Google Guava library
    }

    /**
     * @return TODO: comment this
     */
    public Player getBlackPlayer() {return this.blackPlayer;}

    /**
     * @return TODO: comment this
     */
    public Player getWhitePlayer() {return this.whitePlayer;}

    /**
     * @return TODO: comment this
     */
    public Player getCurrentPlayer() {return this.currentPlayer;}

    /**
     * @return TODO: comment this
     */
    public Collection<Piece> getBlackPieces() {return this.blackPieces;}

    /**
     * @return TODO: comment this
     */
    public Collection<Piece> getWhitePieces() {return this.whitePieces;}

    // INNER CLASS!
    public static class BoardBuilder {

        Map<Integer, Piece> squarePieceMap; // holding Squares "ID" (0-63) and their occupying Piece
        PlayerColor nextToMove;

        /**
         * TODO: comment this
         */
        public BoardBuilder() {this.squarePieceMap = new HashMap<>();}

        /**
         * @param piece at a certain position (i.e. a numbered Square from 0-63).
         * @return an instance of this Builder class.
         */
        public BoardBuilder setPiece(final Piece piece) {
            this.squarePieceMap.put(piece.getPosition(), piece);
            return this;
        }

        /**
         * @param color of the player who's next to move.
         * @return an instance of this Builder class.
         */
        public BoardBuilder setNextToMove(final PlayerColor color) {
            this.nextToMove = color;
            return this;
        }

        /**
         * @return an instance of this Builder class.
         */
        public Board createBoard() {return new Board(this);}

    }

}