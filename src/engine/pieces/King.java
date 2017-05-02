package engine.pieces;

import com.google.common.collect.ImmutableList;
import engine.board.Board;
import engine.board.Square;
import engine.moves.Move;
import engine.moves.Move.CaptureMove;
import engine.moves.Move.NeutralMove;
import engine.players.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.board.BoardUtilities.*;

public class King extends Piece {

    public static final int[] MOVE_PATTERN = {-9, -8, -7, -1,
                                               9,  8,  7,  1}; // offset Squares from current position

    /**
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param color of Player; black or white.
     */
    public King(final int position, final PlayerColor color) {
        super(position, color, PieceType.KING);
    }

    /**
     * @return TODO: comment this
     */
    @Override
    public String toString() {return PieceType.KING.toString();}

    /**
     * @param board TODO: comment this
     * @return a list of legal Moves calculated from valid positions (e.g. not outside Board).
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : MOVE_PATTERN) {
            int possibleMovePosition = this.position + offset; // get position (0-63) of potential move destination position
            if (isValidSquarePosition(possibleMovePosition)) {

                if (isOnColumnA(this.position, offset) ||
                    isOnColumnH(this.position, offset)) {
                    continue; // skip current loop iteration through King's move pattern (i.e. begin next iteration)
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
    public King performMove(final Move move) {
        return new King(
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
        return COLUMN_A[position] && (offset == -9 || offset == -1 || offset == 7);
    }

    /**
     * Valid move offset positions is dependent on certain Columns.
     *
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param offset value from move pattern (i.e. number of Squares from current to potential destination).
     * @return true (if Piece occupies Square on 8th file; move offset thus not valid).
     */
    private static boolean isOnColumnH(final int position, final int offset) {
        return COLUMN_H[position] && (offset == -7 || offset == 1 || offset == 9);
    }

}
