package engine.moves;

import engine.board.Board;
import engine.board.Board.BoardBuilder;
import engine.board.BoardUtilities;
import engine.pieces.Pawn;
import engine.pieces.Piece;
import engine.pieces.Rook;
import engine.players.Player;

import static engine.pieces.Piece.PieceType.QUEEN;

public abstract class Move {

    protected final Board board; // previous Board state (i.e. before Move is performed)
    protected final Piece movedPiece;
    protected final int destPos; // destination position of Piece (fter Move)
    protected final boolean firstMove;
    public static final Move illegalMove = new IllegalMove(); // null Move

    private Move(final Board board, final Piece movedPiece, final int destPos) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destPos = destPos;
        firstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board, final int destPos) {
        this.board = board;
        this.destPos = destPos;
        movedPiece = null;
        firstMove = false;
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
        int i = 1;
        i = 31 * i + destPos;
        i = 31 * i + movedPiece.hashCode();
        i = 31 * i + movedPiece.getPos();
        i = i + (firstMove ? 1 : 0);
        return i;
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
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Move)) {
            return false;
        }
        final Move other = (Move) obj;
        return getCurrPos() == other.getCurrPos() &&
               getDestPos() == other.getDestPos() &&
               getMovedPiece() == other.getMovedPiece();
    }

    public int getCurrPos() {
        if (movedPiece != null) {
            return movedPiece.getPos();
        }
        return 0;
    }
    public int getDestPos() {return destPos;}
    public Board getBoard() {return board;}
    public Piece getMovedPiece() {return movedPiece;}
    public Piece getCaptPiece() {return null;}
    public boolean isCaptureMove() {return false;}
    public boolean isCastlingMove() {return false;}

    /**
     * Loop through all Pieces on Board, create new Board, and set Pieces on it (incl. the moved one).
     * Also, set opposing Player to be next in turn to move.
     * @return a new Board displaying new Piece positions after performed move.
     */
    public Board perform() {
        final BoardBuilder builder = new BoardBuilder();
        final Player currPlayer = board.getCurrPlayer();
        final Player oppPlayer = currPlayer.getOpponent();

        // INCOMING BOARD'S CURRENT PLAYER PIECES
        for (final Piece p : currPlayer.getPieces()) {
            if (!movedPiece.equals(p)) {
                builder.setPiece(p); // .. (all) that wasn't moved, to same positions (but, on new Board)
            }
        }

        // INCOMING BOARD'S OPPONENT PIECES
        for (final Piece piece : oppPlayer.getPieces()) {
            builder.setPiece(piece);
        }

        // RETURNED BOARD'S NEW PIECE POSITIONS
        builder.setPiece(movedPiece.performMove(this));
        builder.setNextToMove(oppPlayer.getColor());
        builder.setMoveTransaction(this);
        return builder.createBoard();
    }

    public Board undo() {
        final BoardBuilder builder = new BoardBuilder();
        for (final Piece p : board.getAllPieces()) {
            builder.setPiece(p);
        }
        builder.setNextToMove(board.getCurrPlayer().getColor());
        return builder.createBoard();
    }

    public String getPGNFromMap() {
        for (final Move move : board.getCurrPlayer().getLegalMoves()) {
            if(move.getDestPos() == destPos && !equals(move) &&
                    movedPiece.getType().equals(move.getMovedPiece().getType())) {
                return BoardUtilities.INSTANCE.getPGNFromPos(movedPiece.getPos()).substring(0, 1);
            }
        }
        return "";
    }

    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("The MoveFactory class is not instantiable!");
        }

        public static Move createMove(final Board board, final int currPos, final int destPos) {

            for (final Move m : board.getAllLegalMoves()) {
                if (m.getCurrPos() == currPos &&
                    m.getDestPos() == destPos) {
                    return m;
                }
            }

            return illegalMove;
        }

    }

    // DEFAULT MOVES
    public static class IllegalMove extends Move {

        private IllegalMove() {
            super(null, -1);
        }

        @Override
        public Board perform() {
            throw new RuntimeException("ERROR: Tried to perform an illegal move!");
        }
        @Override
        public String toString() {
            return "Illegal move";
        }

    }

    public static class NeutralMove extends Move {

        public NeutralMove(final Board board, final Piece movedPiece, final int destPos) {
            super(board, movedPiece, destPos);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof NeutralMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return movedPiece.getType().toString() +
                   getPGNFromMap() +
                   BoardUtilities.INSTANCE.getPGNFromPos(destPos);
        }

    }

    public static abstract class CaptureMove extends Move {

        private final Piece captPiece; // Piece that is being captured by completed Move

        CaptureMove(final Board board, final Piece movedPiece, final int destPos, final Piece captPiece) {
            super(board, movedPiece, destPos);
            this.captPiece = captPiece;
        }

        @Override
        public int hashCode() {
            return this.captPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof CaptureMove)) {
                return false;
            }

            final CaptureMove other = (CaptureMove) obj;
            return super.equals(other) && getCaptPiece().equals(other.getCaptPiece());
        }

        @Override
        public Piece getCaptPiece() {return this.captPiece;}

        @Override
        public boolean isCaptureMove() {return true;}

    }

    public static class NeutralCaptureMove extends CaptureMove {

        public NeutralCaptureMove(final Board board, final Piece movedPiece, final int destPos, final Piece captPiece) {
            super(board, movedPiece, destPos, captPiece);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof NeutralCaptureMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return movedPiece.getType() + getPGNFromMap() + "x" +
                   BoardUtilities.INSTANCE.getPGNFromPos(destPos);
        }

    }

    // CASTLING MOVES
    static abstract class CastlingMove extends Move {

        protected final Rook castlingRook;
        protected final int currPosRook; // current position of Rook (before Castling)
        protected final int destPosRook; // destination position of Rook (after Castling)

        CastlingMove(final Board board, final Piece movedPiece, final int destPos,
                     final Rook castlingRook, final int currPosRook, final int destPosRook) {

            super(board, movedPiece, destPos); // destination position of King (before Castling)
            this.castlingRook = castlingRook;
            this.currPosRook = currPosRook;
            this.destPosRook = destPosRook;
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

            for (final Piece p : this.board.getAllPieces()) {
                if (!this.movedPiece.equals(p) && !this.castlingRook.equals(p)) {
                    builder.setPiece(p);
                }
            }

            builder.setPiece(this.movedPiece.performMove(this));
            builder.setPiece(new Rook(this.destPosRook, this.castlingRook.getColor(),false));
            builder.setNextToMove(this.board.getCurrPlayer().getOpponent().getColor());
            builder.setMoveTransaction(this);

            return builder.createBoard();
        }

        @Override
        public int hashCode() {
            final int i = 31;
            int hashcode = super.hashCode();
            hashcode = i * hashcode + this.castlingRook.hashCode();
            hashcode = i * hashcode + this.destPosRook;
            return hashcode;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof CastlingMove)) {
                return false;
            }

            final CastlingMove other = (CastlingMove) obj;
            return super.equals(other) && this.castlingRook.equals(other.getCastlingRook());
        }

    }

    public static class CastlingShortMove extends CastlingMove { // "kingside castling"

        public CastlingShortMove(final Board board, final Piece movedPiece, final int destPos,
                                 final Rook castlingRook, final int currPosRook, final int destPosRook) {

            super(board, movedPiece, destPos, castlingRook, currPosRook, destPosRook);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof CastlingShortMove)) {
                return false;
            }

            final CastlingShortMove other = (CastlingShortMove) obj;
            return super.equals(other) && this.castlingRook.equals(other.getCastlingRook());
        }

        @Override
        public String toString() {
            return "O-O";
        }

    }

    public static class CastlingLongMove extends CastlingMove {

        public CastlingLongMove(final Board board, final Piece movedPiece, final int destPos,
                                final Rook castlingRook, final int currPosRook, final int destPosRook) {

            super(board, movedPiece, destPos, castlingRook, currPosRook, destPosRook);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof CastlingLongMove)) {
                return false;
            }

            final CastlingLongMove other = (CastlingLongMove) obj;
            return super.equals(other) && this.castlingRook.equals(other.getCastlingRook());
        }

        @Override
        public String toString() {
            return "O-O-O";
        }

    }

    // PAWN MOVES
    public static class PawnMove extends Move {

        public PawnMove(final Board board, final Piece movedPiece, final int destPos) {
            super(board, movedPiece, destPos);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof PawnMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return BoardUtilities.INSTANCE.getPGNFromPos(this.destPos);
        }

    }

    public static class PawnDoubleMove extends Move {

        public PawnDoubleMove(final Board board, final Pawn movedPiece, final int destPos) {
            super(board, movedPiece, destPos);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof PawnDoubleMove && super.equals(obj);
        }

        @Override
        public Board perform() {
            final BoardBuilder builder = new BoardBuilder();

            for (final Piece p : this.board.getCurrPlayer().getPieces()) {
                if (!this.movedPiece.equals(p)) {
                    builder.setPiece(p);
                }
            }

            for (final Piece p : this.board.getCurrPlayer().getOpponent().getPieces()) {
                builder.setPiece(p);
            }

            final Pawn movedPawn = (Pawn) this.movedPiece.performMove(this);

            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setNextToMove(this.board.getCurrPlayer().getOpponent().getColor());
            builder.setMoveTransaction(this);

            return builder.createBoard();
        }

        @Override
        public String toString() {
            return BoardUtilities.INSTANCE.getPGNFromPos(this.destPos);
        }

    }

    public static class PawnCaptureMove extends CaptureMove {

        public PawnCaptureMove(final Board board, final Piece movedPiece, final int destPos, final Piece captPiece) {
            super(board, movedPiece, destPos, captPiece);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof PawnCaptureMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return BoardUtilities.INSTANCE.getPGNFromPos(this.movedPiece.getPos()).substring(0, 1) + "x" +
                   BoardUtilities.INSTANCE.getPGNFromPos(this.destPos);
        }

    }

    public static class PawnEnPassantMove extends PawnCaptureMove {

        public PawnEnPassantMove(final Board board, final Piece movedPiece, final int destPos, final Piece captPiece) {
            super(board, movedPiece, destPos, captPiece);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof PawnEnPassantMove && super.equals(obj);
        }

        @Override
        public Board perform() {
            final BoardBuilder builder = new BoardBuilder();

            for (final Piece p : this.board.getCurrPlayer().getPieces()) {
                if (!this.movedPiece.equals(p)) {
                    builder.setPiece(p);
                }
            }

            for (final Piece piece : this.board.getCurrPlayer().getOpponent().getPieces()) {
                if(!piece.equals(this.getCaptPiece())) {
                    builder.setPiece(piece);
                }
            }

            builder.setPiece(this.movedPiece.performMove(this));
            builder.setNextToMove(this.board.getCurrPlayer().getOpponent().getColor());
            builder.setMoveTransaction(this);

            return builder.createBoard();
        }

        @Override
        public Board undo() {
            final BoardBuilder builder = new BoardBuilder();

            for (final Piece p : this.board.getAllPieces()) {
                builder.setPiece(p);
            }

            builder.setEnPassantPawn((Pawn)this.getCaptPiece());
            builder.setNextToMove(this.board.getCurrPlayer().getColor());

            return builder.createBoard();
        }

    }

    public static class PawnPromotion extends PawnMove {

        final Move move;
        final Pawn pawn;

        public PawnPromotion(final Move move) {
            super(move.getBoard(), move.getMovedPiece(), move.getDestPos());
            this.move = move;
            pawn = (Pawn) move.getMovedPiece();
        }

        @Override
        public int hashCode() {
            return move.hashCode() + (31 * pawn.hashCode());
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof PawnPromotion && (super.equals(obj));
        }

        @Override
        public Board perform() {
            final Board board = this.move.perform();
            final BoardBuilder builder = new BoardBuilder();

            for (final Piece p : board.getCurrPlayer().getPieces()) {
                if (!this.pawn.equals(p)) {
                    builder.setPiece(p);
                }
            }
            for (final Piece piece : board.getCurrPlayer().getOpponent().getPieces()) {
                builder.setPiece(piece);
            }

            builder.setPiece(this.pawn.getPromotedPawn().performMove(this));
            builder.setNextToMove(board.getCurrPlayer().getColor());
            builder.setMoveTransaction(this);

            return builder.createBoard();
        }

        @Override
        public boolean isCaptureMove() {
            return move.isCaptureMove();
        }

        @Override
        public Piece getCaptPiece() {
            return move.getCaptPiece();
        }

        @Override
        public String toString() {
            return BoardUtilities.INSTANCE.getPGNFromPos(movedPiece.getPos()) + "-" +
                   BoardUtilities.INSTANCE.getPGNFromPos(destPos) + "=" + QUEEN;
        }

    }

}