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
import static engine.pieces.Piece.PieceType.KNIGHT;

public class Knight extends Piece {

    private final static int[] MOVE_PATTERN = {-17, -15, -10, -6, 17,  15,  10,  6};

    public Knight(final int position, final PlayerColor color) {
        super(position, color, KNIGHT, true);
    }

    public Knight(final int position, final PlayerColor color, final boolean firstMove) {
        super(position, color, KNIGHT, firstMove);
    }

    @Override
    public String toString() {return KNIGHT.toString();}

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

    @Override
    public Knight performMove(final Move move) {
        return new Knight(move.getDestinationPosition(), move.getMovedPiece().getColor());
    }

    private static boolean isOnColumnA(final int position, final int offset) {
        return COLUMN_A[position] && (offset == -17 || offset == -10 || offset == 6 || offset == 15);
    }

    private static boolean isOnColumnB(final int position, final int offset) {
        return COLUMN_B[position] && (offset == -10 || offset == 6);
    }

    private static boolean isOnColumnG(final int position, final int offset) {
        return COLUMN_G[position] && (offset == 10 || offset == -6);
    }

    private static boolean isOnColumnH(final int position, final int offset) {
        return COLUMN_H[position] && (offset == 17 || offset == 10 || offset == -6 || offset == -15);
    }

}
