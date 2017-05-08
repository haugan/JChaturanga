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
import static engine.players.PlayerColor.BLACK;

public class BlackPlayer extends Player {

    /**
     * @param board TODO: comment this
     * @param blackLegalMoves TODO: comment this
     * @param whiteLegalMoves TODO: comment this
     */
    public BlackPlayer(final Board board,
                       final Collection<Move> blackLegalMoves,
                       final Collection<Move> whiteLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Piece> getPieces() {return this.board.getBlackPieces();}
    public PlayerColor getColor() {return BLACK;}
    public Player getOpponent() {return this.board.getWhitePlayer();}

    @Override
    protected Collection<Move> getCastlingMoves(final Collection<Move> legalMovesPlayer,
                                                final Collection<Move> legalMovesOpponent) {

        final List<Move> castlingMoves = new ArrayList<>();

        if (this.king.isFirstMove() && !this.isChecked()) { // if King hasn't moved yet, and Player isn't in check

            // CASTLING SHORT "KINGSIDE"
            if (!this.board.getSquare(5).isOccupied() &&
                !this.board.getSquare(6).isOccupied()) {

                final Square rookSquareShort = this.board.getSquare(7); // kingside Rook Square

                if (Player.getCaptureMovesOnSquare(5, legalMovesOpponent).isEmpty() &&
                    Player.getCaptureMovesOnSquare(6, legalMovesOpponent).isEmpty() &&
                    rookSquareShort.getPiece().getType() == ROOK) { // see: #26 @15:20 getPiece().isRook()

                    if (rookSquareShort.isOccupied() &&
                        rookSquareShort.getPiece().isFirstMove()) {

                        castlingMoves.add(new CastlingShortMove(this.board,
                                                                this.king,
                                                                6,
                                                                (Rook) rookSquareShort.getPiece(),
                                                                rookSquareShort.getPosition(), // "from position"
                                                                5));        // .. to

                    } // current Rook Square has Piece which hasn't moved yet

                } // castling Squares are free (not under attack), and kingside Square contains a Rook

            } // castling Squares are empty (not occupied by any Piece)

            // CASTLING LONG "QUEENSIDE"
            if (!this.board.getSquare(1).isOccupied() &&
                !this.board.getSquare(2).isOccupied() &&
                !this.board.getSquare(3).isOccupied()) {

                final Square rookSquareLong = this.board.getSquare(0); // queenside Rook Square

                if (Player.getCaptureMovesOnSquare(1, legalMovesOpponent).isEmpty() &&
                    Player.getCaptureMovesOnSquare(2, legalMovesOpponent).isEmpty() &&
                    Player.getCaptureMovesOnSquare(3, legalMovesOpponent).isEmpty() &&
                    rookSquareLong.getPiece().getType() == ROOK) {

                    if (rookSquareLong.isOccupied() &&
                        rookSquareLong.getPiece().isFirstMove()) {

                        castlingMoves.add(new CastlingLongMove(this.board,
                                                               this.king,
                                                               2,
                                                               (Rook) rookSquareLong.getPiece(),
                                                               rookSquareLong.getPosition(), // "from position"
                                                               3));       // .. to

                    } // current Rook Square has Piece which hasn't moved yet

                } // castling Squares are free (not under attack), and kingside Square contains a Rook

            } // castling Squares are empty (not occupied by any Piece)

        }

        return ImmutableList.copyOf(castlingMoves);
    }

}
