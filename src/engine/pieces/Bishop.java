package engine.pieces;

import com.google.common.collect.ImmutableList;
import engine.board.Board;
import engine.board.Square;
import engine.moves.Move;
import engine.moves.Move.NeutralCaptureMove;
import engine.players.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.board.BoardUtilities.*;
import static engine.moves.Move.NeutralMove;
import static engine.pieces.Piece.PieceType.BISHOP;

public class Bishop extends Piece {

    public static final int[] MOVE_PATTERN = {-9, -7, 9,  7};

    public Bishop(final int position, final PlayerColor color) {
        super(position, color, BISHOP, true);
    }

    public Bishop(final int position, final PlayerColor color, final boolean firstMove) {
        super(position, color, BISHOP, firstMove);
    }

    @Override
    public String toString() {return BISHOP.toString();}

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) { // loop through all possible "directions" from the piece's offset pattern
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : MOVE_PATTERN) {
            int possibleMovePosition = this.pos; // // get pos (0-63) of potential move destination pos
            while (isValidSquarePosition(possibleMovePosition)) {

                if (isOnColumnA(pos, offset) || // isValid-rule breaks if piece is at column A or H
                    isOnColumnH(pos, offset)) {
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
                                    new NeutralCaptureMove(board, this, possibleMovePosition, occupyingPiece)
                            );
                        }
                        break; // no need for further checks, an occupied square was found and Bishops can't "jump"
                    }
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Bishop performMove(final Move move) {
        return new Bishop(move.getDestPos(), move.getMovedPiece().getColor());
    }

    private static boolean isOnColumnA(final int position, final int offset) {
        return COLUMN_A[position] && (offset == -9 || offset == 7);
    }

    private static boolean isOnColumnH(final int position, final int offset) {
        return COLUMN_H[position] && (offset == -7 || offset == 9);
    }

}