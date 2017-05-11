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

    public static final int[] MOVE_PATTERN = {-9, -7, 7, 9};

    public Bishop(final int squarePos, final PlayerColor color) {
        super(BISHOP, color, squarePos, true);
    }

    public Bishop(final int squarePos, final PlayerColor color, final boolean firstMove) {
        super(BISHOP, color, squarePos, firstMove);
    }

    @Override
    public String toString() {return BISHOP.toString();}

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) { // loop through all possible "directions" from the piece's offset pattern
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : MOVE_PATTERN) {

            int destPos = this.squarePos; // // get Square number (0-63) of potential move destination poition

            while (isSquareOnBoard(destPos)) {

                if (isOnColumnA(destPos, offset) || // isValid-rule breaks if piece is at column A or H
                    isOnColumnH(destPos, offset)) {
                    break; // out of while-loop (i.e. on to next offset vector value from Bishop's move pattern)
                }

                destPos += offset; // add offset vector values from move pattern

                if (isSquareOnBoard(destPos)) { // go further only for the values that are in bounds
                    final Square destSquare = board.getSquare(destPos);

                    // NEUTRAL MOVE
                    if (!destSquare.isOccupied()) { // possible square destination for move is empty
                        legalMoves.add(new NeutralMove(board, this, destPos));

                    // CAPTURE
                    } else {
                        final Piece destPiece = destSquare.getPiece();
                        final PlayerColor destPieceColor = destPiece.getColor();

                        if (color != destPieceColor) {
                            legalMoves.add(new NeutralCaptureMove(board, this, destPos, destPiece));
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
        return PieceUtilities.INSTANCE.getMovedBishop(move); // return new Bishop to new Board
    }

    private static boolean isOnColumnA(final int destPos, final int offset) {
        return COLUMN_A.get(destPos) && ((offset == -9) || (offset == 7));
    }

    private static boolean isOnColumnH(final int destPos, final int offset) {
        return COLUMN_H.get(destPos) && ((offset == -7) || (offset == 9));
    }

}