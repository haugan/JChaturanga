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

    /**
     * @param board TODO: comment this
     * @param movedPiece to be created at a numbered destination Square on a new Board.
     * @param destinationPosition numbered position of the Square that is moved to.
     */
    private Move(final Board board,
                 final Piece movedPiece,
                 final int destinationPosition) {
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
        final int prime = 31;
        int hashCode = 1;
        hashCode = prime * hashCode + this.getDestinationPosition();
        hashCode = prime * hashCode + this.movedPiece.hashCode();
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

    public boolean isCaptureMove() {return false;}
    public boolean isCastlingMove() {return false;}

    public Piece getCapturedPiece() {return null;}
    public Piece getMovedPiece() {return this.movedPiece;}

    /**
     * @return numbered position of the Square a Piece is moved from.
     */
    public int getCurrentPosition() {return this.getMovedPiece().getPosition();}

    /**
     * @return numbered position of the Square a Piece is moved to.
     */
    public int getDestinationPosition() {return this.destinationPosition;}

    /**
     * Loop through all Pieces on Board, create new Board, and set Pieces on it (incl. the moved one).
     * Also, set opposing Player to be next in turn to move.
     * @return a new Board displaying new Piece positions after performed move.
     */
    public Board perform() {
        final BoardBuilder builder = new BoardBuilder();

        // INCOMING BOARD'S CURRENT PLAYER PIECES
        for (final Piece p : this.board.getCurrentPlayer().getPieces()) {
            if (!this.movedPiece.equals(p)) {
                builder.setPiece(p); // .. (all) that wasn't moved, to same positions (but, on new Board)
            }
        }

        // INCOMING BOARD'S OPPONENT PIECES
        for (final Piece p : this.board.getCurrentPlayer().getOpponent().getPieces()) {
            builder.setPiece(p); // .. (all) that wasn't moved, to same positions (but, on new Board)
        }

        // RETURNED BOARD'S NEW PIECE POSITIONS
        builder.setPiece(
                this.movedPiece.performMove(this)
        );

        builder.setNextToMove(this.board.getCurrentPlayer().getOpponent().getColor());
        return builder.createBoard();
    }

    // INNER CLASS!
    public static class MoveFactory {

        private MoveFactory() {throw new RuntimeException("The MoveFactory class is not instantiable!");}

        public static Move createMove(final Board board,
                                      final int currentPosition, // ..from Square number i
                                      final int destinationPosition) { // ..to Square number i

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
        } // representing a "null" Move

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
        /**
         * @param board TODO: comment this
         * @param movedPiece to be created at a numbered destination Square on a new Board.
         * @param destinationPosition numbered position of the Square that is moved to.
         */
        public NeutralMove(final Board board,
                           final Piece movedPiece,
                           final int destinationPosition) {
            super(board, movedPiece, destinationPosition);
        }
    }

    // INNER CLASS!
    public static final class PawnNeutralSingleMove extends Move {
        /**
         * @param board TODO: comment this
         * @param movedPiece to be created at a numbered destination Square on a new Board.
         * @param destinationPosition numbered position of the Square that is moved to.
         */
        public PawnNeutralSingleMove(final Board board,
                                     final Piece movedPiece,
                                     final int destinationPosition) {
            super(board, movedPiece, destinationPosition);
        }
    }

    // INNER CLASS!
    public static final class PawnNeutralDoubleMove extends Move {
        /**
         * @param board TODO: comment this
         * @param movedPiece to be created at a numbered destination Square on a new Board.
         * @param destinationPosition numbered position of the Square that is moved to.
         */
        public PawnNeutralDoubleMove(final Board board,
                                     final Piece movedPiece,
                                     final int destinationPosition) {
            super(board, movedPiece, destinationPosition);
        }

        @Override
        public Board perform() {
            final BoardBuilder b = new BoardBuilder();
            for (final Piece p : this.board.getCurrentPlayer().getPieces()) {
                if (!this.movedPiece.equals(p)) b.setPiece(p);
            }
            for (final Piece p : this.board.getCurrentPlayer().getOpponent().getPieces()) {
                b.setPiece(p);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.performMove(this);
            b.setPiece(movedPawn);
            b.setEnPassantPawn(movedPawn);
            b.setNextToMove(this.board.getCurrentPlayer().getOpponent().getColor());
            return b.createBoard();
        }
    }

    // INNER CLASS!
    public static class CaptureMove extends Move {
        final Piece capturedPiece;

        /**
         * @param board TODO: comment this
         * @param movedPiece to be created at a numbered destination Square on a new Board.
         * @param destinationPosition numbered position of the Square that is moved to.
         * @param capturedPiece to be removed from new Board (?).
         */
        public CaptureMove(final Board board,
                           final Piece movedPiece,
                           final int destinationPosition,
                           final Piece capturedPiece) {
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
        public boolean isCaptureMove() {return true;}

        @Override
        public Piece getCapturedPiece() {return this.capturedPiece;}
    }

    // INNER CLASS!
    public static class PawnCaptureMove extends CaptureMove {
        /**
         * @param board TODO: comment this
         * @param movedPiece to be created at a numbered destination Square on a new Board.
         * @param destinationPosition numbered position of the Square that is moved to.
         */
        public PawnCaptureMove(final Board board,
                               final Piece movedPiece,
                               final int destinationPosition,
                               final Piece capturedPiece) {
            super(board, movedPiece, destinationPosition, capturedPiece);
        }
    }

    // INNER CLASS!
    public static final class EnPassantMove extends PawnCaptureMove {
        /**
         * @param board TODO: comment this
         * @param movedPiece to be created at a numbered destination Square on a new Board.
         * @param destinationPosition numbered position of the Square that is moved to.
         */
        public EnPassantMove(final Board board,
                             final Piece movedPiece,
                             final int destinationPosition,
                             final Piece capturedPiece) {
            super(board, movedPiece, destinationPosition, capturedPiece);
        }
    }

    // INNER CLASS!
    static abstract class CastlingMove extends Move {

        protected final Rook castlingRook;
        protected final int rookPositionCurrent;
        protected final int rookPositionDestination;

        /**
         * @param board TODO: comment this
         * @param movedPiece to be created at a numbered destination Square on a new Board.
         * @param destinationPosition numbered position of the Square that is moved to.
         */
        public CastlingMove(final Board board,
                            final Piece movedPiece,
                            final int destinationPosition,
                            final Rook castlingRook,
                            final int rookPositionCurrent,
                            final int rookPositionDestination) {
            super(board, movedPiece, destinationPosition);
            this.castlingRook = castlingRook;
            this.rookPositionCurrent = rookPositionCurrent;
            this.rookPositionDestination = rookPositionDestination;
        }

        public Rook getCastlingRook() {
            return this.castlingRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board perform() {
            final BoardBuilder builder = new BoardBuilder();

            // INCOMING BOARD'S CURRENT PLAYER PIECES
            for (final Piece p : this.board.getCurrentPlayer().getPieces()) {
                if (!this.movedPiece.equals(p) &&
                    !this.castlingRook.equals(p)) {
                    builder.setPiece(p); // .. (all) that wasn't moved, to same positions (but, on new Board)
                }
            }

            // INCOMING BOARD'S OPPONENT PIECES
            for (final Piece p : this.board.getCurrentPlayer().getOpponent().getPieces()) {
                builder.setPiece(p); // .. (all) that wasn't moved, to same positions (but, on new Board)
            }

            builder.setPiece(
                    this.movedPiece.performMove(this)
            ); // the moved King

            builder.setPiece(
                    new Rook(this.rookPositionDestination, this.castlingRook.getColor())
            ); // create new Piece (representing the moved Rook)

            builder.setNextToMove(this.board.getCurrentPlayer().getOpponent().getColor());
            return builder.createBoard();
        }
    }

    // INNER CLASS!
    public static final class CastlingLongMove extends CastlingMove {
        /**
         * Queenside castling.
         * @param board TODO: comment this
         * @param movedPiece to be created at a numbered destination Square on a new Board.
         * @param destinationPosition numbered position of the Square that is moved to.
         */
        public CastlingLongMove(final Board board,
                                final Piece movedPiece,
                                final int destinationPosition,
                                final Rook castlingRook,
                                final int rookPositionCurrent,
                                final int rookPositionDestination) {
            super(board, movedPiece, destinationPosition, castlingRook, rookPositionCurrent, rookPositionDestination);
        }

        @Override
        public String toString() {
            return "O-O-O"; // PGN notation for castling long
        }
    }

    // INNER CLASS!
    public static final class CastlingShortMove extends CastlingMove {
        /**
         * Kingside castling.
         * @param board TODO: comment this
         * @param movedPiece to be created at a numbered destination Square on a new Board.
         * @param destinationPosition numbered position of the Square that is moved to.
         */
        public CastlingShortMove(final Board board,
                                 final Piece movedPiece,
                                 final int destinationPosition,
                                 final Rook castlingRook,
                                 final int rookPositionCurrent,
                                 final int rookPositionDestination) {
            super(board, movedPiece, destinationPosition, castlingRook, rookPositionCurrent, rookPositionDestination);
        }

        @Override
        public String toString() {
            return "O-O"; // PGN notation for castling short
        }
    }

}