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
import static engine.pieces.Piece.PieceType.KING;

public class King extends Piece {

    private static final int[] MOVE_PATTERN = {-9, -8, -7, -1, 1, 7, 8, 9};



    public King(final int squarePos, final PlayerColor color) {
        super(KING, color, squarePos,true);
    }

    public King(final int squarePos, final PlayerColor color, final boolean firstMove) {
        super(KING, color, squarePos, firstMove);
    }

    @Override
    public String toString() {return KING.toString();}

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : MOVE_PATTERN) {

            if (isOnColumnA(squarePos, offset) ||
                isOnColumnH(squarePos, offset)) {
                continue; // skip current loop iteration through King's move pattern (i.e. begin next iteration)
            }

            int destPos = squarePos + offset; // get squarePos (0-63) of potential move destination squarePos
            if (isSquareOnBoard(destPos)) {

                final Square destSquare = board.getSquare(destPos);
                if (!destSquare.isOccupied()) { // possible square destination for move is empty
                    legalMoves.add(new NeutralMove(board, this, destPos));
                } else {
                    final Piece destPiece = destSquare.getPiece();
                    final PlayerColor destColor = destPiece.getColor();
                    if (color != destColor) { // occupying piece is enemy's
                        legalMoves.add(new NeutralCaptureMove(board, this, destPos, destPiece));
                    }
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King performMove(final Move move) {
        return new King(move.getDestPos(), move.getMovedPiece().getColor());
    }

    private static boolean isOnColumnA(final int destPos, final int offset) {
        //return COLUMN_A[position] && (offset == -9 || offset == -1 || offset == 7);
        return COLUMN_A.get(destPos) && ((offset == -9) || (offset == -1) || (offset == 7));
    }

    private static boolean isOnColumnH(final int destPos, final int offset) {
        //return COLUMN_H[position] && (offset == -7 || offset == 1 || offset == 9);
        return COLUMN_H.get(destPos) && ((offset == -7) || (offset == 1) || (offset == 9));
    }

}
