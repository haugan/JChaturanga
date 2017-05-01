package engine.pieces;

import engine.board.Board;
import engine.board.Square;
import engine.moves.Move;
import engine.players.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.board.BoardUtilities.*;
import static engine.board.BoardUtilities.COLUMN_H;
import static engine.moves.Move.*;
import static java.util.Collections.unmodifiableList;

public class Bishop extends Piece {

    public static final int[] MOVE_PATTERN = {-9, -7, 7, 9}; // offset from position

    /**
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param playerColor black or white.
     */
    public Bishop(final int position, final PlayerColor playerColor) {
        super(position, playerColor, PieceType.BISHOP);
    }

    /**
     * @return TODO: comment this!
     */
    @Override
    public String toString() {return PieceType.BISHOP.toString();}

    /**
     * @param board TODO: comment this!
     * @return a list of legal Moves calculated from valid positions (e.g. not outside Board).
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) { // loop through all possible "directions" from the piece's offset pattern
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : MOVE_PATTERN) {
            int possibleMovePosition = this.position; // // get position (0-63) of potential move destination position
            while (isValidSquarePosition(possibleMovePosition)) {

                if (isOnColumnA(position, offset) || // isValid-rule breaks if piece is at column A or H
                    isOnColumnH(position, offset)) {
                    break; // out of while-loop (i.e. on to next offset vector value from Bishop's move pattern)
                }

                possibleMovePosition += offset; // add offset vector values from move pattern

                if (isValidSquarePosition(possibleMovePosition)) { // go further only for the values that are in bounds
                    final Square possibleSquareDestination = board.getSquare(possibleMovePosition);
                    if (!possibleSquareDestination.isOccupied()) { // possible square destination for move is empty
                        legalMoves.add(
                                new NeutralMove(board, this, possibleMovePosition)
                        );
                    } else {
                        final Piece occupyingPiece = possibleSquareDestination.getPiece();
                        final PlayerColor occupyingColor = occupyingPiece.getColor();
                        if (this.color != occupyingColor) { // occupying piece is enemy's
                            legalMoves.add(
                                    new CaptureMove(board, this, possibleMovePosition, occupyingPiece)
                            );
                        }
                        break; // no need for further checks, an occupied square was found and Bishops can't "jump"
                    }
                }

            }
        }

        return unmodifiableList(legalMoves);
    }

    /**
     * @param move object containing new position for Piece, and color of "moving" Player.
     * @return Piece that will move to new Board.
     */
    @Override
    public Bishop movePiece(final Move move) {
        return new Bishop(
                move.getDestinationPosition(),
                move.getMovedPiece().getColor()
        );
    }

    /**
     * Valid move offset positions is dependent on certain Columns.
     *
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param offset value from move pattern (i.e. number of Squares from current to potential destination).
     * @return true (if Piece occupies Square on 1st file; move offset thus not valid).
     */
    private static boolean isOnColumnA(final int position, final int offset) {
        return COLUMN_A[position] && (offset == -9 || offset == 7);
    }

    /**
     * Valid move offset positions is dependent on certain Columns.
     *
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param offset value from move pattern (i.e. number of Squares from current to potential destination).
     * @return true (if Piece occupies Square on 8th file; move offset thus not valid).
     */
    private static boolean isOnColumnH(final int position, final int offset) {
        return COLUMN_H[position] && (offset == -7 || offset == 9);
    }

}