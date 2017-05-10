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

    public static final int[] MOVE_PATTERN = {-9, -8, -7, -1, 9,  8,  7,  1};

    public King(final int position, final PlayerColor color) {
        super(position, color, PieceType.KING);
    }

    @Override
    public String toString() {return PieceType.KING.toString();}

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

    @Override
    public King performMove(final Move move) {
        return new King(
                move.getDestinationPosition(),
                move.getMovedPiece().getColor()
        );
    }

    private static boolean isOnColumnA(final int position, final int offset) {
        return COLUMN_A[position] && (offset == -9 || offset == -1 || offset == 7);
    }

    private static boolean isOnColumnH(final int position, final int offset) {
        return COLUMN_H[position] && (offset == -7 || offset == 1 || offset == 9);
    }

}
