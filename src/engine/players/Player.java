package engine.players;

import com.google.common.collect.ImmutableList;
import engine.board.Board;
import engine.moves.Move;
import engine.moves.MoveTransaction;
import engine.pieces.King;
import engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.moves.MoveTransactionResult.*;
import static engine.pieces.Piece.PieceType.KING;

public abstract class Player {

    protected final Board board;
    protected final King king;
    protected final Collection<Move> legalMoves;
    private final boolean inCheck;

    /**
     * @param board TODO: comment this
     * @param legalMovesPlayer TODO: comment this
     * @param legalMovesOpponent TODO: comment this
     */
    protected Player(final Board board,
                     final Collection<Move> legalMovesPlayer,
                     final Collection<Move> legalMovesOpponent) {
        this.board = board;
        this.king = initializeKing();
        this.legalMoves = legalMovesPlayer;

        /*
            Reasons for not having Boolean values for "isCheckmated/isStalemated" at instantation:
            - "Chicken & egg" problem.
            - Players are constructed when constructing a Board.
            - For calculating checkmate, "canEscape()" gets called.
            - The method performs all legal Moves to find potential escape Moves,
              and for that to happen it creates another Board (to perform the Moves on)
              which in turn causes the Board class to instantiate more Players, and so on..
         */

        this.inCheck = !Player.isAttemptingCapture(
                this.king.getPosition(), legalMovesOpponent
        ).isEmpty(); // current Player is in check if returned list contains legal Moves
    }

    /**
     * @return the current Player's King Piece.
     */
    public King getKing() {return this.king;}

    /**
     * @return TODO: comment this
     */
    public Collection<Move> getLegalMoves() {return this.legalMoves;}

    // TODO: comment these
    public abstract Collection<Piece> getPieces();
    public abstract PlayerColor getColor();
    public abstract Player getOpponent();

    /**
     * Check opponent's legal Moves for possible check positions.
     * @param position TODO: comment this
     * @param legalMovesOpponent TODO: comment this
     * @return list of Moves that captures (i.e. overlaps destination position with) Player's current King-position.
     */
    private static Collection<Move> isAttemptingCapture(final int position,
                                                        final Collection<Move> legalMovesOpponent) {
        final List<Move> legalCaptureMovesOpponent = new ArrayList<>();
        for (final Move m : legalMovesOpponent) {
            if (position == m.getDestinationPosition()) {
                legalCaptureMovesOpponent.add(m);
            }
        }

        return ImmutableList.copyOf(legalCaptureMovesOpponent);
    }

    /**
     * @param move that is to be performed by Player.
     * @return object wrapping the new Board with new positioning.
     */
    public MoveTransaction performMove(final Move move) {
        // MOVE CANCELED
        if (!isAttemptingLegalMove(move)) {
            return new MoveTransaction(
                    this.board, move, CANCELED
            ); // return same Board as before attempted Move (i.e. current positioning)
        }

        // PERFORM MOVE ON NEW BOARD TO LOOK FOR CHECKED POSITIONS
        final Board newBoard = move.perform(); // new Board switches current Player to "next color"

        // MOVE LEAVES PLAYER IN CHECK
        Collection<Move> checkMoves = isAttemptingCapture(
                newBoard.getCurrentPlayer()
                        .getOpponent()
                        .getKing()
                        .getPosition(),
                newBoard.getCurrentPlayer()
                        .getLegalMoves()
        ); // find capture attempts against current Player's King (after move, current player is now the opponent)
        if (!checkMoves.isEmpty()) {
            return new MoveTransaction(this.board, move, PLAYER_CHECKED);
        }

        // MOVE OK
        return new MoveTransaction(newBoard, move, COMPLETED); // return new Board with new positions
    }

    /**
     * @param attemptedMove by Player.
     * @return true (if attempted Move is contained in Player's current list of legal Moves).
     */
    public boolean isAttemptingLegalMove(final Move attemptedMove) {return this.legalMoves.contains(attemptedMove);}

    /**
     * @return a King Piece object from a list of active "in-game" Pieces currently on the Board.
     */
    public King initializeKing() {
        for (final Piece p : getPieces()) {
            if (p.getType() == KING) { // alternative solution #17 @15:30 ? getType().isKing()
                return (King) p;
            }
        }

        throw new RuntimeException("Chess board is not valid without the King piece!"); // preferred unreachable
    }

    /**
     * @return true (if current Player's King is in checked position).
     */
    public boolean isChecked() {return this.inCheck;}

    /**
     * @return true (if Player's King is in checked position).
     */
    public boolean isCheckmated() {return this.inCheck && !this.canEscape();}

    /**
     * @return true (if all legal moves puts Player's King in checked position).
     */
    public boolean isStalemated() {return !this.inCheck && !this.canEscape();}

    // TODO: implement this
    public boolean isCastled() {return false;}

    /**
     * Check if Player can escape a checked position, by performing legal Moves on a separate Board.
     * @return true (if any of the Player's current legal Moves removes him from check by opponent).
     */
    protected boolean canEscape() {
        for (final Move m : this.legalMoves) {
            final MoveTransaction transaction = performMove(m);
            if (transaction.getResult().isMovePerformed()) {
                return true;
            }
        }

        return false;
    }

}
