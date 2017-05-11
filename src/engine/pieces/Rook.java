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
import static engine.pieces.Piece.PieceType.ROOK;

public class Rook extends Piece {

    public static final int[] MOVE_PATTERN = {-8, -1, 8,  1}; // vector values for each horizontal & vertical direction

    public Rook(final int pos, final PlayerColor color) {
        super(pos, color, ROOK, true);
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
            int destPos = this.pos; // set pos (0-63) of potential move

            while (isValidSquarePosition(destPos)) {

                if (isOnColumnA(pos, offset) || // isValid-rule breaks if piece is at column A or H
                    isOnColumnH(pos, offset)) {
                    break; // out of while-loop (i.e. on to next offset vector value from Rook's move pattern)
                }

                destPos += offset; // add offset vector values from move pattern

                if (isValidSquarePosition(destPos)) { // go further only for the values that are in bounds
                    final Square destSquare = board.getSquare(destPos); // destination Square for move

                    if (!destSquare.isOccupied()) { // possible Square destination for Move is empty
                        legalMoves.add(new NeutralMove(board, this, destPos));
                    } else {
                        final Piece occPiece = destSquare.getPiece(); // occupying Piece at Square destination
                        final PlayerColor color = occPiece.getColor();
                        if (this.color != color) { // occupying Piece at Square is opposing Player's
                            legalMoves.add(new NeutralCaptureMove(board, this, destPos, occPiece));
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
        return new Rook(move.getDestPos(), move.getMovedPiece().getColor());
    }

    private static boolean isOnColumnA(final int position, final int offset) {
        return COLUMN_A[position] && (offset == -1);
    }

    private static boolean isOnColumnH(final int position, final int offset) {
        return COLUMN_H[position] && (offset == 1);
    }

}
