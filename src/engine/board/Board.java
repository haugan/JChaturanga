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
import static engine.players.PlayerColor.BLACK;
import static engine.players.PlayerColor.WHITE;

public class Board {

    private final List<Square> squareList;
    private final Collection<Piece> blackPieces;
    private final Collection<Piece> whitePieces;
    private final BlackPlayer blackPlayer;
    private final WhitePlayer whitePlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;
    private final Move transactionMove;

    private Board(final BoardBuilder builder) {
        squareList = createSquareList(builder);
        blackPieces = getIngamePieces(builder, BLACK);
        whitePieces = getIngamePieces(builder, WHITE);
        Collection<Move> blackLegalMoves = getLegalMovesCurrentPlayer(blackPieces);
        Collection<Move> whiteLegalMoves = getLegalMovesCurrentPlayer(whitePieces);
        blackPlayer = new BlackPlayer(this, blackLegalMoves, whiteLegalMoves);
        whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        currentPlayer = builder.nextToMove.setPlayer(whitePlayer, blackPlayer);
        enPassantPawn = builder.enPassantPawn; // TODO: implement
        transactionMove = (builder.transactionMove != null) ? builder.transactionMove : Move.illegalMove;
    }

    /**
     * Get Squares from list, call each overridden toString method for displaying Console graphics.
     * @return formatted ASCII graphics of chess board.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (int pos = 0; pos < SQUARES_ON_BOARD; pos++) { // loop through each pos on Board
            final String square = this.squareList.get(pos).toString(); // overridden in Square class
            sb.append(String.format("%3s", square));
            if ((pos + 1) % SQUARES_ON_ROW == 0) {
                sb.append("\n"); // new line each 8th Square
            }
        }

        return sb.toString();
    }

    public Collection<Piece> getBlackPieces() {return blackPieces;}
    public Collection<Piece> getWhitePieces() {return whitePieces;}
    public Player getBlackPlayer() {return blackPlayer;}
    public Player getWhitePlayer() {return whitePlayer;}
    public Player getCurrPlayer() {return currentPlayer;}
    public Pawn getEnPassantPawn() {return enPassantPawn;}
    public Move getTransactionMove() {return transactionMove;}
    public Square getSquare(final int position) {return squareList.get(position);}
    public List<Square> getSquareList() {return squareList;}

    /**
     * Create the initial chess board positioning.
     * @return an instance of this class.
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
        b.setPiece(new Queen(59, WHITE));
        b.setPiece(new King(60, WHITE));
        b.setPiece(new Bishop(61, WHITE));
        b.setPiece(new Knight(62, WHITE));
        b.setPiece(new Rook(63, WHITE));

        b.setNextToMove(WHITE);
        return b.createBoard();
    }

    /**
     * Loop through each pos on Board (0-63) and create Squares (both empty & occupied).
     * @param builder TODO: comment this
     * @return list of 64 Squares representing the tiles on a chess board.
     */
    private static List<Square> createSquareList(final BoardBuilder builder) {
        final Square[] squares = new Square[SQUARES_ON_BOARD];

        for (int pos = 0; pos < SQUARES_ON_BOARD; pos++) { // loop through each pos on Board
            squares[pos] = Square.createSquare(pos, builder.squarePieceMap.get(pos)
            ); // get Piece associated with pos, and create Square (with occupying Piece)
        }

        return ImmutableList.copyOf(squares);
    }

    private static Collection<Piece> getIngamePieces(final BoardBuilder builder, final PlayerColor color) {
        final List<Piece> ingamePieces = new ArrayList<>(16);
        for (final Piece p : builder.squarePieceMap.values()) {
            if (p.getColor() == color) {
                ingamePieces.add(p);
            }
        }
        return ImmutableList.copyOf(ingamePieces);
    }

    public Iterable<Piece> getAllPieces() {
        return Iterables.unmodifiableIterable(
                Iterables.concat(blackPieces,
                                 whitePieces)
        );
    }

    public Collection<Move> getLegalMovesCurrentPlayer(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>(35);
        for (final Piece p : pieces) {
            legalMoves.addAll(p.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    public Iterable<Move> getLegalMovesBothPlayers() {
        return Iterables.unmodifiableIterable(
                Iterables.concat(
                        whitePlayer.getLegalMoves(),
                        blackPlayer.getLegalMoves()
                )
        );
    }

    // INNER CLASS!
    public static class BoardBuilder {

        private final Map<Integer, Piece> squarePieceMap; // holding Squares "ID" (0-63) and their occupying Piece
        private PlayerColor nextToMove;
        private Pawn enPassantPawn; // holds Pawn that could be captured "en passant" by opposing Player
        private Move transactionMove;

        public BoardBuilder() {
            squarePieceMap = new HashMap<>(33, 1.0f);
        }

        public Board createBoard() {
            return new Board(this);
        }

        /**
         * @param piece at a certain pos (i.e. a numbered Square from 0-63).
         * @return an instance of this Builder class.
         */
        public BoardBuilder setPiece(final Piece piece) {
            squarePieceMap.put(piece.getPos(), piece);
            return this;
        }

        public BoardBuilder setNextToMove(final PlayerColor color) {
            nextToMove = color;
            return this;
        }

        public BoardBuilder setEnPassantPawn(final Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
            return this;
        }

        public BoardBuilder setMoveTransaction(final Move transactionMove) {
            this.transactionMove = transactionMove;
            return this;
        }

    }

}