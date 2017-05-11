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
import static engine.pieces.Piece.PieceType.ROOK;

public class Rook extends Piece {

    public static final int[] MOVE_PATTERN = {-8, -1, 8,  1}; // vector values for each horizontal & vertical direction

    public Rook(final int position, final PlayerColor color) {
        super(position, color, ROOK, true);
    }

    public Rook(final int position, final PlayerColor color, final boolean firstMove) {
        super(position, color, ROOK, firstMove);

    }

    @Override
    public String toString() {return ROOK.toString();}

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) { // loop through all possible "directions" from the piece's offset pattern
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : MOVE_PATTERN) {
            int possibleMovePosition = this.position; // set position (0-63) of potential move
            while (isValidSquarePosition(possibleMovePosition)) {

                if (isOnColumnA(position, offset) || // isValid-rule breaks if piece is at column A or H
                    isOnColumnH(position, offset)) {
                    break; // out of while-loop (i.e. on to next offset vector value from Rook's move pattern)
                }

                possibleMovePosition += offset; // add offset vector values from move pattern

                if (isValidSquarePosition(possibleMovePosition)) { // go further only for the values that are in bounds
                    final Square possibleSquareDestination = board.getSquare(possibleMovePosition);
                    if (!possibleSquareDestination.isOccupied()) { // possible square destination for move is empty
                        legalMoves.add(new NeutralMove(board, this, possibleMovePosition));
                    } else {
                        final Piece occupyingPiece = possibleSquareDestination.getPiece();
                        final PlayerColor occupyingColor = occupyingPiece.getColor();
                        if (this.color != occupyingColor) { // occupying piece is enemy's
                            legalMoves.add(new CaptureMove(board, this, possibleMovePosition, occupyingPiece));
                        }
                        break; // no need for further checks, an occupied square was found and Rooks can't "jump"
                    }
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Rook performMove(final Move move) {
        return new Rook(move.getDestinationPosition(), move.getMovedPiece().getColor());
    }

    private static boolean isOnColumnA(final int position, final int offset) {
        return COLUMN_A[position] && (offset == -1);
    }

    private static boolean isOnColumnH(final int position, final int offset) {
        return COLUMN_H[position] && (offset == 1);
    }

}
