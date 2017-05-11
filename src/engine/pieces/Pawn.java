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
import static engine.players.PlayerColor.BLACK;
import static engine.players.PlayerColor.WHITE;

public class Pawn extends Piece {

    private static final int[] OFFSET_PATTERN = {7, 8, 9, 16}; // .. Squares from current pos
    private int currentPosition; // current numbered Square pos of Piece
    private PlayerColor color;

    public Pawn(final int currentPosition, final PlayerColor color) {
        super(PAWN, currentPosition, color, true);
        this.currentPosition = currentPosition;
        this.color = color;
    }

    public Pawn(final int currentPosition, final PlayerColor color, final boolean firstMove) {
        super(PAWN, currentPosition, color, firstMove);
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
            } else if (offset == 16 && isFirstMove() &&
                      ((ROW_7[currentPosition] && color == BLACK) ||
                       (ROW_2[currentPosition] && color == WHITE))) {

                final int jumpedPos = currentPosition + (color.getMoveDirection() * 8);
                final Square jumpedSquare = board.getSquare(jumpedPos);

                if (!jumpedSquare.isOccupied() && !destSquare.isOccupied()) {
                    legalMoves.add(new PawnDoubleMove(board, this, movePos));
                }

            // DIAGONAL CAPTURE "WEST"
            } else if (offset == 7 &&
                      !((COLUMN_A[currentPosition] && color == BLACK ||
                        (COLUMN_H[currentPosition] && color == WHITE))
                      )) {

                addPawnCaptureMove(board, legalMoves, movePos, destSquare);


                // DIAGONAL CAPTURE "EAST"
            } else if (offset == 9 &&
                      !((COLUMN_A[currentPosition] && color == WHITE) ||
                        (COLUMN_H[currentPosition] && color == BLACK)
                      )) {

                addPawnCaptureMove(board, legalMoves, movePos, destSquare);

            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    private void addPawnCaptureMove(Board board, List<Move> legalMoves, int movePos, Square destSquare) {
        if (destSquare.isOccupied()) {
            final Piece destPiece = destSquare.getPiece();
            if (color != destPiece.getColor()) {
                legalMoves.add(new PawnCaptureMove(board, this, movePos, destPiece));
            }
        }
    }

    public Piece getPromotedPawn() {
        return new Queen(pos, color, false);
    }

    @Override
    public Pawn performMove(final Move move) {
        return new Pawn(move.getDestPos(), move.getMovedPiece().getColor());
    }

    @Override
    public String toString() {return PAWN.toString();}

}
