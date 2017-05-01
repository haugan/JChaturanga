package engine.pieces;

import com.google.common.collect.ImmutableList;
import engine.board.Board;
import engine.moves.Move;
import engine.moves.Move.CaptureMove;
import engine.moves.Move.NeutralMove;
import engine.players.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.board.BoardUtilities.*;

public class Pawn extends Piece {

    public static final int[] MOVE_PATTERN = {7, 8, 9, 16}; // offset Squares from current position

    /**
     * @param position of Square from top-left to bottom-right; 0 to 63.
     * @param color of Player; black or white.
     */
    public Pawn(final int position, final PlayerColor color) {
        super(position, color, PieceType.PAWN);
    }

    /**
     * @return TODO: comment this!
     */
    @Override
    public String toString() {return PieceType.PAWN.toString();}

    /**
     * @param board TODO: comment this!
     * @return a list of legal Moves calculated from valid positions (e.g. not outside Board).
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : MOVE_PATTERN) {
            final int possibleMovePosition =
                    this.position + (
                            this.color.getMoveDirection() * offset
                    ); // constants of -1 and 1 from PlayerColor give correct move pattern values for both directions

            if (!isValidSquarePosition(possibleMovePosition)) {
                continue; // if position is out of bounds, jump to next iteration of for-loop
            }

            if (offset == 8 && !board.getSquare(possibleMovePosition).isOccupied()) { // neutral move
                legalMoves.add(
                        new NeutralMove(board, this, possibleMovePosition)
                );

            } else if (offset == 16 && isFirstMove() &&
                      (ROW_7[this.position] && getColor().isBlack()) ||
                      (ROW_2[this.position] && getColor().isWhite())) { // jump move

                final int possibleBetweenPosition = this.position +
                                                   (this.color.getMoveDirection() * 8); // position that's jumped over

                if (!board.getSquare(possibleBetweenPosition).isOccupied() &&
                    !board.getSquare(possibleMovePosition).isOccupied()) { // both Squares are free, Pawn can jump
                    legalMoves.add(
                            new NeutralMove(board, this, possibleMovePosition)
                    );
                }

            } else if (offset == 7 &&
                      !(
                        (COLUMN_A[this.position] && this.color.isBlack()) ||
                        (COLUMN_H[this.position] && this.color.isWhite())
                      )) { // diagonal capture move is possible (i.e. in bounds)

                addDiagonalCaptureMove(board, legalMoves, possibleMovePosition);

            } else if (offset == 9 &&
                      !(
                        (COLUMN_A[this.position] && this.color.isWhite()) ||
                        (COLUMN_H[this.position] && this.color.isBlack())
                      )) { // diagonal capture move is possible (i.e. in bounds)

                addDiagonalCaptureMove(board, legalMoves, possibleMovePosition);

            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * @param move object containing new position for Piece, and color of "moving" Player.
     * @return Piece that will move to new Board.
     */
    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(
                move.getDestinationPosition(),
                move.getMovedPiece().getColor()
        );
    }

    /**
     * @param board TODO: comment this
     * @param legalMoves calculated from valid positions (e.g. not outside Board).
     * @param possibleMovePosition representing position of the Square a Piece moves to (0-63).
     */
    private void addDiagonalCaptureMove(final Board board,
                                        final List<Move> legalMoves,
                                        final int possibleMovePosition) {

        if (board.getSquare(possibleMovePosition).isOccupied()) { // some Piece occupying destination Square
            final Piece occupyingPiece = board.getSquare(possibleMovePosition).getPiece();
            if (this.color != occupyingPiece.color) { // enemy Piece occupying Square
                legalMoves.add(
                        new CaptureMove(board, this, possibleMovePosition, occupyingPiece)
                );
            }
        }

    }

}
