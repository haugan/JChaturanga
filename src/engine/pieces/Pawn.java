package engine.pieces;

import com.google.common.collect.ImmutableList;
import engine.board.Board;
import engine.board.Square;
import engine.moves.Move;
import engine.moves.Move.NeutralMove;
import engine.moves.Move.PawnCaptureMove;
import engine.moves.Move.PawnDoubleMove;
import engine.players.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.board.BoardUtilities.*;
import static engine.pieces.Piece.PieceType.PAWN;

public class Pawn extends Piece {

    private static final int[] OFFSET_PATTERN = {7, 8, 9, 16}; // .. Squares from current position
    private int currentPosition; // current numbered Square position of Piece
    private PlayerColor color;

    public Pawn(final int currentPosition, final PlayerColor color) {
        super(currentPosition, color, PAWN);
        this.currentPosition = currentPosition;
        this.color = color;
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : OFFSET_PATTERN) {

            final int movePos = currentPosition + (color.getMoveDirection() * offset); // direction values (up -1 / down +1)
            final Square destSquare = board.getSquare(movePos); // move destination Square

            // SKIP EVERYTHING IF MOVE POSITION IS OUTSIDE BOARD
            if (!isValidSquarePosition(movePos)) {
                continue; // if currPos is out of bounds, jump to next iteration of for-loop
            }

            // SINGLE MOVE
            if (offset == 8 && !destSquare.isOccupied()) { // neutral move
                legalMoves.add(new NeutralMove(board, this, movePos));

            // DOUBLE JUMP
            } else if (offset == 16 &&
                      ((ROW_7[currentPosition] && getColor().isBlack()) ||
                       (ROW_2[currentPosition] && getColor().isWhite()))) {

                final int jumpedPos = currentPosition + (color.getMoveDirection() * 8);
                final Square jumpedSquare = board.getSquare(jumpedPos);

                if (!jumpedSquare.isOccupied() && !destSquare.isOccupied()) {
                    legalMoves.add(new PawnDoubleMove(board, this, movePos));
                }

            // DIAGONAL CAPTURE "WEST"
            } else if (offset == 7 && destSquare.isOccupied() &&
                      !((COLUMN_A[currentPosition] && color.isBlack()) || (COLUMN_H[currentPosition] && color.isWhite()))) {
                addPawnCaptureMove(board, legalMoves, movePos, destSquare);

            // DIAGONAL CAPTURE "EAST"
            } else if (offset == 9 && destSquare.isOccupied() &&
                      !((COLUMN_A[currentPosition] && color.isWhite()) || (COLUMN_H[currentPosition] && color.isBlack()))) {
                addPawnCaptureMove(board, legalMoves, movePos, destSquare);
            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    private void addPawnCaptureMove(Board board, List<Move> legalMoves, int movePos, Square destSquare) {
        final Piece destPiece = destSquare.getPiece();
        if (color != destPiece.getColor()) {
            legalMoves.add(new PawnCaptureMove(board, this, movePos, destPiece));
        }
    }

    @Override
    public Pawn performMove(final Move move) {
        return new Pawn(move.getDestinationPosition(), move.getMovedPiece().getColor());
    }

    @Override
    public String toString() {return PAWN.toString();}

}
