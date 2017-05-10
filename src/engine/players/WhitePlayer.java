package engine.players;

import com.google.common.collect.ImmutableList;
import engine.board.Board;
import engine.board.Square;
import engine.moves.Move;
import engine.moves.Move.CastlingLongMove;
import engine.moves.Move.CastlingShortMove;
import engine.pieces.Piece;
import engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static engine.pieces.Piece.PieceType.ROOK;
import static engine.players.PlayerColor.WHITE;

public class WhitePlayer extends Player {

    public WhitePlayer(final Board board,
                       final Collection<Move> whiteLegalMoves,
                       final Collection<Move> blackLegalMoves) {

        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> getPieces() {return this.board.getWhitePieces();}

    @Override
    public PlayerColor getColor() {return WHITE;}

    @Override
    public Player getOpponent() {return this.board.getBlackPlayer();}

    @Override
    protected Collection<Move> getCastlingMoves(final Collection<Move> legalMovesPlayer,
                                                final Collection<Move> legalMovesOpponent) {

        final List<Move> castlingMoves = new ArrayList<>();

        if (king.getIsFirstMove() && !isChecked()) { // if King hasn't moved yet, and Player isn't in check

            // CASTLING SHORT "KINGSIDE"
            if (!board.getSquare(61).isOccupied() &&
                !board.getSquare(62).isOccupied()) {

                final Square rookSquareShort = board.getSquare(63); // kingside Rook Square

                if (rookSquareShort.isOccupied() && rookSquareShort.getPiece().getIsFirstMove()) {

                    if (Player.getCaptureMovesOnSquare(61, legalMovesOpponent).isEmpty() &&
                        Player.getCaptureMovesOnSquare(62, legalMovesOpponent).isEmpty() &&
                        rookSquareShort.getPiece().getType() == ROOK) { // see: #26 @15:20 getPiece().isRook()

                        castlingMoves.add(
                                new CastlingShortMove(
                                        board,
                                        king,
                                        62,
                                        (Rook) rookSquareShort.getPiece(),
                                        rookSquareShort.getPosition(), // from position
                                        61)         // to destination
                        );

                    } // castling Squares are free (not under attack), and kingside Square contains a Rook

                } // kingside Square has Piece which hasn't moved yet

            } // castling Squares are empty (not occupied by any Piece)

            // CASTLING LONG "QUEENSIDE"
            if (!board.getSquare(59).isOccupied() &&
                !board.getSquare(58).isOccupied() &&
                !board.getSquare(57).isOccupied()) {

                final Square rookSquareLong = board.getSquare(56); // queenside Rook Square

                if (rookSquareLong.isOccupied() && rookSquareLong.getPiece().getIsFirstMove()) {

                    if (Player.getCaptureMovesOnSquare(59, legalMovesOpponent).isEmpty() &&
                        Player.getCaptureMovesOnSquare(58, legalMovesOpponent).isEmpty() &&
                        Player.getCaptureMovesOnSquare(57, legalMovesOpponent).isEmpty() &&
                        rookSquareLong.getPiece().getType() == ROOK) {

                        castlingMoves.add(
                                new CastlingLongMove(
                                        board,
                                        king,
                                        58,
                                        (Rook) rookSquareLong.getPiece(),
                                        rookSquareLong.getPosition(), // from position
                                        59)        // to destination
                        );

                    } // castling Squares are free (not under attack), and kingside Square contains a Rook

                } // queenside Square has Piece which hasn't moved yet

            } // castling Squares are empty (not occupied by any Piece)

        }

        return ImmutableList.copyOf(castlingMoves);
    }

}
