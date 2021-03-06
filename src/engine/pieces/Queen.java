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
import static engine.pieces.Piece.PieceType.QUEEN;

public class Queen extends Piece {

    public static final int[] MOVE_PATTERN = {-9, -8, -7, -1, 9,  8,  7,  1}; // offset Squares from current squarePos

    public Queen(final int squarePos, final PlayerColor color) {
        super(QUEEN, color, squarePos, true);
    }

    public Queen(final int squarePos, final PlayerColor color, final boolean firstMove) {
        super(QUEEN, color, squarePos, firstMove);
    }

    @Override
    public String toString() {return QUEEN.toString();}

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) { // loop through all possible "directions" from the piece's offset pattern
        final List<Move> legalMoves = new ArrayList<>();

        for (final int offset : MOVE_PATTERN) {
            int possibleMovePosition = this.squarePos; // get squarePos (0-63) of potential move destination squarePos
            while (isSquareOnBoard(possibleMovePosition)) {

                if (isOnColumnA(squarePos, offset) || // isValid-rule breaks if Piece is at column A or H
                    isOnColumnH(squarePos, offset)) {
                    break; // out of while-loop (i.e. on to next offset vector value from Queen's move pattern)
                }

                possibleMovePosition += offset; // add offset vector values from move pattern

                if (isSquareOnBoard(possibleMovePosition)) { // go further only for the values that are in bounds
                    final Square possibleSquareDestination = board.getSquare(possibleMovePosition);
                    if (!possibleSquareDestination.isOccupied()) { // possible Square destination for move is empty
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
                        break; // no need for further checks, occupied Square was found and Queens can't "jump"
                    }
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Queen performMove(final Move move) {
        return PieceUtilities.INSTANCE.getMovedQueen(move); // return new Queen to new Board
    }

    private static boolean isOnColumnA(final int destPos, final int offset) {
        return COLUMN_A.get(destPos) && (offset == -9 || offset == -1 || offset == 7);
    }

    private static boolean isOnColumnH(final int destPos, final int offset) {
        return COLUMN_H.get(destPos) && (offset == -7 || offset == 1 || offset == 9);
    }

}
