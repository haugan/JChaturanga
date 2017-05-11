package engine.pieces;

import com.google.common.collect.ImmutableList;
import engine.board.Board;
import engine.board.Square;
import engine.moves.Move;
import engine.moves.Move.NeutralCaptureMove;
import engine.moves.Move.NeutralMove;
import engine.players.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.board.BoardUtilities.*;
import static engine.pieces.Piece.PieceType.KNIGHT;

public class Knight extends Piece {

    private final static int[] MOVE_PATTERN = {-17, -15, -10, -6, 17,  15,  10,  6};

    public Knight(final int squarePos, final PlayerColor color) {
        super(KNIGHT, color, squarePos, true);
    }

    public Knight(final int squarePos, final PlayerColor color, final boolean firstMove) {
        super(KNIGHT, color, squarePos, firstMove);
    }

    @Override
    public String toString() {return KNIGHT.toString();}

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : MOVE_PATTERN) { // loop through all possible moves from the piece's offset pattern
            final int possibleMovePosition = this.squarePos + offset; // get squarePos (0-63) of potential move destination squarePos
            if (isSquareOnBoard(possibleMovePosition)) {

                if (isOnColumnA(squarePos, offset) ||
                    isOnColumnB(squarePos, offset) ||
                    isOnColumnG(squarePos, offset) ||
                    isOnColumnH(squarePos, offset)) {
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
                                new NeutralCaptureMove(board, this, possibleMovePosition, occupyingPiece)
                        );
                    }
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight performMove(final Move move) {
        return PieceUtilities.INSTANCE.getMovedKnight(move); // return new Knight to new Board
    }

    private static boolean isOnColumnA(final int destPos, final int offset) {
        return COLUMN_A.get(destPos) && (offset == -17 || offset == -10 || offset == 6 || offset == 15);
    }

    private static boolean isOnColumnB(final int destPos, final int offset) {
        return COLUMN_B.get(destPos) && (offset == -10 || offset == 6);
    }

    private static boolean isOnColumnG(final int destPos, final int offset) {
        return COLUMN_G.get(destPos) && (offset == 10 || offset == -6);
    }

    private static boolean isOnColumnH(final int destPos, final int offset) {
        return COLUMN_H.get(destPos) && (offset == 17 || offset == 10 || offset == -6 || offset == -15);
    }

}
