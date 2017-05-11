package engine.players;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
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

    protected Player(final Board board,
                     final Collection<Move> legalMovesPlayer, final Collection<Move> legalMovesOpponent) {

        this.board = board;
        king = initializeKing();

        legalMoves = ImmutableList.copyOf(
                Iterables.concat(legalMovesPlayer, getCastlingMoves(legalMovesPlayer, legalMovesOpponent))
        );

        /*
        Reasons for not having Boolean values for "isCheckmated/isStalemated" at instantation:
        - "Chicken & egg" problem.
        - Players are constructed when constructing a Board.
        - For calculating checkmate, "canEscape()" gets called.
        - The method performs all legal Moves to find potential escape Moves,
          and for that to happen it creates another Board (to perform the Moves on)
          which in turn causes the Board class to instantiate more Players, and so on..
        */
        inCheck = !Player.getCaptureMovesOnSquare(
                king.getSquarePos(), legalMovesOpponent
        ).isEmpty(); // current Player is in check if returned list contains legal Moves
    }

    public abstract Collection<Piece> getPieces();
    public abstract PlayerColor getColor();
    public abstract Player getOpponent();
    protected abstract Collection<Move> getCastlingMoves(final Collection<Move> legalMovesPlayer,
                                                         final Collection<Move> legalMovesOpponent);
    public King getKing() {return king;}
    public Collection<Move> getLegalMoves() {return legalMoves;}

    /**
     * Check if a given Square squarePos is capturable by any legal Moves from opposing Player.
     * @param position of Square to check if any legal capture move can reach.
     * @param legalMovesOpponent collected by Move type.
     * @return list of Moves that overlaps their destination squarePos with current Player's Piece-squarePos.
     */
    protected static Collection<Move> getCaptureMovesOnSquare(final int position,
                                                              final Collection<Move> legalMovesOpponent) {

        final List<Move> captureMovesOpponent = new ArrayList<>();
        for (final Move m : legalMovesOpponent) {
            if (position == m.getDestPos()) {
                captureMovesOpponent.add(m);
            }
        }

        return ImmutableList.copyOf(captureMovesOpponent);
    }

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
     * @param move that is to be performed by Player.
     * @return object wrapping the new Board with new positioning.
     */
    public MoveTransaction performMove(final Move move) {

        // MOVE IS NOT LEGAL
        if (!isAttemptingLegalMove(move)) {
            return new MoveTransaction(board, move, CANCELED
            ); // return same Board as before attempted Move (i.e. current positioning)
        }

        // PERFORM MOVE ON NEW BOARD, LOOK FOR POSITIONS WHERE KING IS CHECKED
        final Board newBoard = move.perform(); // new Board switches current Player to "next color"

        // MOVE LEAVES PLAYER IN CHECK
        Collection<Move> checkMoves = getCaptureMovesOnSquare(newBoard.getCurrPlayer()
                                                                      .getOpponent()
                                                                      .getKing()
                                                                      .getSquarePos(),
                                                              newBoard.getCurrPlayer()
                                                                      .getLegalMoves()
        ); // get check attempts against opponent's King (after move, current player is the opponent)

        if (!checkMoves.isEmpty()) {
            return new MoveTransaction(board, move, PLAYER_CHECKED);
        } // if list isn't empty, the King is checked

        // MOVE IS LEGAL
        return new MoveTransaction(newBoard, move, COMPLETED); // return new Board with new positions
    }

    public boolean isAttemptingLegalMove(final Move attemptedMove) {return legalMoves.contains(attemptedMove);}

    public boolean isChecked() {return inCheck;}
    public boolean isCheckmated() {return inCheck && !canEscape();}
    public boolean isStalemated() {return !inCheck && !canEscape();}
    public boolean isCastled() {return false;}

    /**
     * Check if Player can escape a checked squarePos, by performing legal Moves on a separate Board.
     * @return true (if any of the Player's current legal Moves removes him from check by opponent).
     */
    protected boolean canEscape() {
        for (final Move m : legalMoves) {
            final MoveTransaction transaction = performMove(m);
            if (transaction.getResult() == COMPLETED) {
                return true;
            }
        }
        return false;
    }

}
