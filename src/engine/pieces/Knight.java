package engine.pieces;

import com.google.common.collect.ImmutableList;
import engine.board.Board;
import engine.board.Square;
import engine.moves.Move;
import engine.moves.Move.NeutralMove;
import engine.players.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.board.BoardUtilities.*;
import static engine.moves.Move.CaptureMove;

public class Knight extends Piece {

    private final static int[] MOVE_PATTERN = {-17, -15, -10, -6,
                                                17,  15,  10,  6}; // offset from position

    /**
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param color of Player; black or white.
     */
    public Knight(final int position, final PlayerColor color) {
        super(position, color, PieceType.KNIGHT);
    }

    /**
     * @return TODO: comment this
     */
    @Override
    public String toString() {return PieceType.KNIGHT.toString();}

    /**
     * @param board TODO: comment this
     * @return a list of legal Moves calculated from valid positions (e.g. not outside Board).
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : MOVE_PATTERN) { // loop through all possible moves from the piece's offset pattern
            final int possibleMovePosition = this.position + offset; // get position (0-63) of potential move destination position
            if (isValidSquarePosition(possibleMovePosition)) {

                if (isOnColumnA(position, offset) ||
                    isOnColumnB(position, offset) ||
                    isOnColumnG(position, offset) ||
                    isOnColumnH(position, offset)) {
                    continue; // skip current loop iteration through Knight's move pattern (i.e. begin next iteration)
                }

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
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * @param move object containing new position for Piece, and color of "moving" Player.
     * @return Piece that will move to new Board.
     */
    @Override
    public Knight movePiece(final Move move) {
        return new Knight(
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
        return COLUMN_A[position] && (offset == -17 || offset == -10 || offset == 6 || offset == 15);
    }

    /**
     * Valid move offset positions is dependent on certain Columns.
     *
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param offset value from move pattern (i.e. number of Squares from current to potential destination).
     * @return true (if Piece occupies Square on 2nd file; move offset thus not valid).
     */
    private static boolean isOnColumnB(final int position, final int offset) {
        return COLUMN_B[position] && (offset == -10 || offset == 6);
    }

    /**
     * Valid move offset positions is dependent on certain Columns.
     *
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param offset value from move pattern (i.e. number of Squares from current to potential destination).
     * @return true (if Piece occupies Square on 7th file; move offset thus not valid).
     */
    private static boolean isOnColumnG(final int position, final int offset) {
        return COLUMN_G[position] && (offset == 10 || offset == -6);
    }

    /**
     * Valid move offset positions is dependent on certain Columns.
     *
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param offset value from move pattern (i.e. number of Squares from current to potential destination).
     * @return true (if Piece occupies Square on 8th file; move offset thus not valid).
     */
    private static boolean isOnColumnH(final int position, final int offset) {
        return COLUMN_H[position] && (offset == 17 || offset == 10 || offset == -6 || offset == -15);
    }

}
