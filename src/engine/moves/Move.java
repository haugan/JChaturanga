package engine.moves;

import engine.board.Board;
import engine.board.Board.BoardBuilder;
import engine.pieces.Pawn;
import engine.pieces.Piece;
import engine.pieces.Rook;

public abstract class Move {

    final Board board; // previous Board state (i.e. before Move is performed)
    final Piece movedPiece;
    final int destinationPosition;
    public static final Move ILLEGAL_MOVE = new IllegalMove(); // null Move

    private Move(final Board board, final Piece movedPiece, final int destinationPosition) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationPosition = destinationPosition;
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
    public int hashCode() {
        final int i = 31;
        int hashCode = 1;
        hashCode = i * hashCode + this.getDestinationPosition();
        hashCode = i * hashCode + this.movedPiece.hashCode();
        return hashCode;
    }

    /**
     * Overridden from JRE to test for object equality over default reference-only equality test.
     * A Piece object copy might have changed from the original (e.g. new position on Board).
     * (ref. http://tutorials.jenkov.com/java-collections/hashcode-equals.html)
     * @param obj that is tested for equality.
     * @return true (if the two objects has equal "states", i.e. same variable values, etc.)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Move)) return false;
        final Move other = (Move) obj;
        return
                this.getDestinationPosition() == other.getDestinationPosition() &&
                this.getMovedPiece().equals(other.getMovedPiece());
    }

    private int getCurrentPosition() {return getMovedPiece().getPosition();}
    public int getDestinationPosition() {return destinationPosition;}
    public Piece getMovedPiece() {return movedPiece;}
    public Piece getCapturedPiece() {return null;}
    public boolean isCaptureMove() {return false;}
    public boolean isCastlingMove() {return false;}

    /**
     * Loop through all Pieces on Board, create new Board, and set Pieces on it (incl. the moved one).
     * Also, set opposing Player to be next in turn to move.
     * @return a new Board displaying new Piece positions after performed move.
     */
    public Board perform() {
        final BoardBuilder bB = new BoardBuilder();

        // INCOMING BOARD'S CURRENT PLAYER PIECES
        for (final Piece p : board.getCurrentPlayer().getPieces()) {
            if (!this.movedPiece.equals(p)) {
                bB.setPiece(p); // .. (all) that wasn't moved, to same positions (but, on new Board)
            }
        }

        // INCOMING BOARD'S OPPONENT PIECES
        for (final Piece p : board.getCurrentPlayer().getOpponent().getPieces()) {
            bB.setPiece(p); // .. (all) that wasn't moved, to same positions (but, on new Board)
        }

        // RETURNED BOARD'S NEW PIECE POSITIONS
        bB.setPiece(movedPiece.performMove(this));

        bB.setNextToMove(board.getCurrentPlayer().getOpponent().getColor());
        return bB.createBoard();
    }

    // INNER CLASS!
    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("The MoveFactory class is not instantiable!");
        }

        public static Move createMove(final Board board,
                                      final int currentPosition, final int destinationPosition) {
            for (final Move m : board.getBothPlayersLegalMoves()) {
                if (m.getCurrentPosition() == currentPosition &&
                    m.getDestinationPosition() == destinationPosition) {
                    return m;
                }
            }
            return ILLEGAL_MOVE;
        }

    }

    // INNER CLASS!
    public static final class IllegalMove extends Move {

        public IllegalMove() {
            super(null, null, -1);
        }

        @Override
        public Board perform() {
            throw new RuntimeException(
                    "The IllegalMove class is not instantiable!\n" +
                    "(An illegal move was attempted to perform!)"
            );
        }
    }

    // INNER CLASS!
    public static final class NeutralMove extends Move {

        public NeutralMove(final Board board, final Piece movedPiece, final int destinationPosition) {
            super(board, movedPiece, destinationPosition);
        }

    }

    // INNER CLASS!
    public static final class PawnDoubleMove extends Move {

        public PawnDoubleMove(final Board board, final Piece movedPiece, final int destinationPosition) {
            super(board, movedPiece, destinationPosition);
        }

        @Override
        public Board perform() {
            final BoardBuilder bB = new BoardBuilder();

            for (final Piece p : this.board.getCurrentPlayer().getPieces()) {
                if (!this.movedPiece.equals(p)) bB.setPiece(p);
            }

            for (final Piece p : this.board.getCurrentPlayer().getOpponent().getPieces()) {
                bB.setPiece(p);
            }

            final Pawn movedPawn = (Pawn) this.movedPiece.performMove(this);
            bB.setPiece(movedPawn);
            bB.setEnPassantPawn(movedPawn);

            bB.setNextToMove(this.board.getCurrentPlayer().getOpponent().getColor());
            return bB.createBoard();
        }
    }

    // INNER CLASS!
    public static class CaptureMove extends Move {

        final Piece capturedPiece;

        public CaptureMove(final Board board, final Piece movedPiece,
                           final int destinationPosition, final Piece capturedPiece) {

            super(board, movedPiece, destinationPosition);
            this.capturedPiece = capturedPiece;
        }

        @Override
        public int hashCode() {return this.capturedPiece.hashCode() + super.hashCode();}

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof CaptureMove)) return false;
            final CaptureMove other = (CaptureMove) obj;
            return
                super.equals(other) &&
                this.getCapturedPiece().equals(other.getCapturedPiece());
        }

        @Override
        public Board perform() {return null;}

        @Override
        public Piece getCapturedPiece() {return this.capturedPiece;}

        @Override
        public boolean isCaptureMove() {return true;}
    }

    // INNER CLASS!
    public static class PawnCaptureMove extends CaptureMove {

        public PawnCaptureMove(final Board board, final Piece movedPiece,
                               final int destinationPosition, final Piece capturedPiece) {

            super(board, movedPiece, destinationPosition, capturedPiece);
        }

    }

    // INNER CLASS!
    public static final class EnPassantMove extends PawnCaptureMove {

        public EnPassantMove(final Board board, final Piece movedPiece,
                             final int destinationPosition, final Piece capturedPiece) {

            super(board, movedPiece, destinationPosition, capturedPiece);
        }

    }

    // INNER CLASS!
    static abstract class CastlingMove extends Move {

        private final Rook castlingRook;
        private final int rookPositionCurrent;
        private final int rookPositionDestination;

        public CastlingMove(final Board board, final Piece movedPiece,
                            final int destinationPosition, final Rook castlingRook,
                            final int rookPositionCurrent, final int rookPositionDestination) {

            super(board, movedPiece, destinationPosition);
            this.castlingRook = castlingRook;
            this.rookPositionCurrent = rookPositionCurrent;
            this.rookPositionDestination = rookPositionDestination;
        }

        public Rook getCastlingRook() {return this.castlingRook;}

        @Override
        public boolean isCastlingMove() {return true;}

        @Override
        public Board perform() {
            final BoardBuilder builder = new BoardBuilder();

            // INCOMING BOARD'S CURRENT PLAYER PIECES
            for (final Piece p : this.board.getCurrentPlayer().getPieces()) {
                if (!this.movedPiece.equals(p) && !this.castlingRook.equals(p)) {
                    builder.setPiece(p); // .. (all) that wasn't moved, to same positions (but, on new Board)
                }
            }

            // INCOMING BOARD'S OPPONENT PIECES
            for (final Piece p : this.board.getCurrentPlayer().getOpponent().getPieces()) {
                builder.setPiece(p); // .. (all) that wasn't moved, to same positions (but, on new Board)
            }

            builder.setPiece(this.movedPiece.performMove(this)); // the moved King
            builder.setPiece(new Rook(this.rookPositionDestination,
                                 this.castlingRook.getColor())
            ); // create new Piece (representing the moved Rook)

            builder.setNextToMove(this.board.getCurrentPlayer().getOpponent().getColor());
            return builder.createBoard();
        }
    }

    // INNER CLASS!
    public static final class CastlingLongMove extends CastlingMove {

        public CastlingLongMove(final Board board, final Piece king,
                                final int kingPositionDestination, final Rook castlingRook,
                                final int rookPositionCurrent, final int rookPositionDestination) {

            super(board, king, kingPositionDestination,
                  castlingRook, rookPositionCurrent, rookPositionDestination);
        }

        @Override
        public String toString() {
            return "O-O-O"; // PGN notation for Queen-side castling
        }
    }

    // INNER CLASS!
    public static final class CastlingShortMove extends CastlingMove {

        public CastlingShortMove(final Board board, final Piece king,
                                 final int kingPositionDestination, final Rook castlingRook,
                                 final int rookPositionCurrent, final int rookPositionDestination) {

            super(board, king, kingPositionDestination,
                  castlingRook, rookPositionCurrent, rookPositionDestination);
        }

        @Override
        public String toString() {
            return "O-O"; // PGN notation for King-side castling
        }
    }

}