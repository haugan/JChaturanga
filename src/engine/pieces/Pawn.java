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
import static engine.pieces.Piece.PieceType.PAWN;

public class Pawn extends Piece {

    private static final int[] OFFSET_PATTERN = {7, 8, 9, 16}; // .. Squares from current position
    private int currPos; // current numbered Square position of Piece
    private PlayerColor color;
    private final boolean isFirstMove;

    public Pawn(final int currentPosition, final PlayerColor color, boolean isFirstMove) {
        super(currentPosition, color, PAWN);
        currPos = currentPosition;
        this.color = color;
        this.isFirstMove = isFirstMove;
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : OFFSET_PATTERN) {

            final int movePos = currPos + (color.getMoveDirection() * offset); // direction values (up -1 / down +1)
            final Square destSquare = board.getSquare(movePos); // move destination Square

            // SKIP EVERYTHING IF MOVE POSITION IS OUTSIDE BOARD
            if (!isValidSquarePosition(movePos)) {
                continue; // if currPos is out of bounds, jump to next iteration of for-loop
            }

            // SINGLE MOVE
            if (offset == 8 && !destSquare.isOccupied()) { // neutral move
                legalMoves.add(new NeutralMove(board, this, movePos));

            // DOUBLE JUMP
            } else if (offset == 16 && this.isFirstMove() &&
                      ((ROW_7[currPos] && getColor().isBlack()) || (ROW_2[currPos] && getColor().isWhite()))
            ) {

                final int jumpedPos = currPos + (color.getMoveDirection() * 8);
                final Square jumpedSquare = board.getSquare(jumpedPos);

                if (!jumpedSquare.isOccupied() && !destSquare.isOccupied()) {
                    legalMoves.add(new NeutralMove(board, this, movePos));
                }

            // DIAGONAL CAPTURE "WEST"
            } else if (offset == 7 && destSquare.isOccupied() &&
                      !((COLUMN_A[currPos] && color.isBlack()) || (COLUMN_H[currPos] && color.isWhite()))) {

                // TODO: if (move position contains opposing piece)
                if (color != destSquare.getPiece().getColor()) {
                    addDiagonalCaptureMove(board, legalMoves, movePos);
                }

            // DIAGONAL CAPTURE "EAST"
            } else if (offset == 9 && destSquare.isOccupied() &&
                      !((COLUMN_A[currPos] && color.isWhite()) || (COLUMN_H[currPos] && color.isBlack()))) {

                // TODO: if (move position contains opposing piece)
                if (color != destSquare.getPiece().getColor()) {
                    addDiagonalCaptureMove(board, legalMoves, movePos);
                }
            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn performMove(final Move move) {
        return new Pawn(move.getDestinationPosition(), move.getMovedPiece().getColor(), false);
    }

    @Override
    public String toString() {return PAWN.toString();}

    private void addDiagonalCaptureMove(final Board board, final List<Move> legalMoves, final int movePosition) {
        if (board.getSquare(movePosition).isOccupied()) { // some Piece occupying destination Square
            final Piece occupyingPiece = board.getSquare(movePosition).getPiece();
            if (color != occupyingPiece.color) { // enemy Piece occupying Square
                legalMoves.add(
                        new CaptureMove(board, this, movePosition, occupyingPiece)
                );
            }
        }

    }

}
