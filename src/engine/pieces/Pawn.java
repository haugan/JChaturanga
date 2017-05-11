package engine.pieces;

import com.google.common.collect.ImmutableList;
import engine.board.Board;
import engine.moves.Move;
import engine.moves.Move.*;
import engine.players.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.board.BoardUtilities.*;
import static engine.pieces.Piece.PieceType.PAWN;

public final class Pawn extends Piece {

    private static final int[] OFFSET_PATTERN = {7, 8, 9, 16}; // .. Squares from current squarePos


    public Pawn(final int currPos, final PlayerColor color) {
        super(PAWN, color, currPos,true);
    }

    public Pawn(final int currPos, final PlayerColor color, final boolean firstMove) {
        super(PAWN, color, currPos, firstMove);
    }



    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : OFFSET_PATTERN) {

            int destPos = squarePos + (color.getMoveDir() * offset); // direction values (up -1 / down +1)

            // SKIP EVERYTHING IF MOVE POSITION IS OUTSIDE BOARD
            if (!isSquareOnBoard(destPos)) {
                continue; // if squarePos is out of bounds, jump to next iteration of for-loop
            }

            // SINGLE MOVE
            if (offset == 8 && !board.getSquare(destPos).isOccupied()) { // neutral move

                // PROMOTION
                if (color.hasReachedPromotion(destPos)) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, destPos)));
                } else {
                    legalMoves.add(new PawnMove(board, this, destPos));
                }

            // DOUBLE JUMP
            } else if (offset == 16 && isFirstMove() &&
                      ((ROW_7.get(squarePos) && color.isBlack()) || (ROW_2.get(squarePos) && color.isWhite()))) {

                final int jumpedPos = squarePos + (color.getMoveDir() * 8);

                if (!board.getSquare(jumpedPos).isOccupied() && !board.getSquare(destPos).isOccupied()) {
                    legalMoves.add(new PawnDoubleMove(board, this, destPos));
                }

            // DIAGONAL CAPTURE "WEST"
            } else if (offset == 7 &&
                      !((COLUMN_H.get(squarePos) && color.isWhite()) || (COLUMN_A.get(squarePos) && color.isBlack()))) {

                if (board.getSquare(destPos).isOccupied()) {
                    final Piece destPiece = board.getSquare(destPos).getPiece();
                    if (color != destPiece.getColor()) {

                        // PROMOTION
                        if (color.hasReachedPromotion(destPos)) {
                            legalMoves.add(new PawnPromotion(new PawnCaptureMove(board, this, destPos, destPiece)));
                        }
                        // CAPTURE
                        else {
                            legalMoves.add(new PawnCaptureMove(board, this, destPos, destPiece));
                        }

                    }

                // EN PASSANT "WEST"
                } else if (board.getEnPassantPawn() != null &&
                           board.getEnPassantPawn().getSquarePos() == (squarePos + (color.getOppDir()))) {

                    final Piece destPiece = board.getEnPassantPawn();
                    if (color != destPiece.getColor()) {

                        // PROMOTION
                        if (color.hasReachedPromotion(destPos)) {
                            legalMoves.add(new PawnPromotion(new PawnEnPassantMove(board, this, destPos, destPiece)));
                        }
                        // CAPTURE
                        else {
                            legalMoves.add(new PawnEnPassantMove(board, this, destPos, destPiece));
                        }

                    }
                }

            // DIAGONAL CAPTURE "EAST"
            } else if (offset == 9 &&
                      !((COLUMN_A.get(squarePos) && color.isWhite()) || (COLUMN_H.get(squarePos) && color.isBlack()))) {

                if (board.getSquare(destPos).isOccupied()) {

                    if (color != board.getSquare(destPos).getPiece().getColor()) {
                        if (color.hasReachedPromotion(destPos)) {
                            legalMoves.add(new PawnPromotion(new PawnCaptureMove(board, this, destPos, board.getSquare(destPos).getPiece())));
                        } else {
                            legalMoves.add(new PawnCaptureMove(board, this, destPos, board.getSquare(destPos).getPiece()));
                        }
                    }

                // EN PASSANT "EAST"
                } else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getSquarePos() == (squarePos - (color.getOppDir()))) {

                    final Piece destPiece = board.getEnPassantPawn();

                    if (color != destPiece.getColor()) {

                        // PROMOTION
                        if (color.hasReachedPromotion(destPos)) {
                            legalMoves.add(new PawnPromotion(new PawnEnPassantMove(board, this, destPos, destPiece)));
                        }
                        // CAPTURE
                        else {
                            legalMoves.add(new PawnEnPassantMove(board, this, destPos, destPiece));
                        }

                    }

                }

            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    public Piece getPromotionQueen() {
        return new Queen(squarePos, color, false);
    }

    @Override
    public Pawn performMove(final Move move) {
        return PieceUtilities.INSTANCE.getMovedPawn(move); // return new Pawn to new Board
    }

    @Override
    public String toString() {return PAWN.toString();}

}
