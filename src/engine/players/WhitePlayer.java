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

    /**
     * @param board TODO: comment this
     * @param whiteLegalMoves TODO: comment this
     * @param blackLegalMoves TODO: comment this
     */
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteLegalMoves,
                       final Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> getPieces() {return this.board.getWhitePieces();}
    public PlayerColor getColor() {return WHITE;}
    public Player getOpponent() {return this.board.getBlackPlayer();}

    @Override
    protected Collection<Move> getCastlingMoves(final Collection<Move> legalMovesPlayer,
                                                final Collection<Move> legalMovesOpponent) {

        final List<Move> castlingMoves = new ArrayList<>();

        if (this.king.isFirstMove() && !this.isChecked()) {
            // CASTLING SHORT "KINGSIDE"
            if (!this.board.getSquare(61).isOccupied() &&
                !this.board.getSquare(62).isOccupied()) {
                final Square rookSquareShort = this.board.getSquare(63);
                if (Player.getCaptureMovesOnSquare(61, legalMovesOpponent).isEmpty() &&
                    Player.getCaptureMovesOnSquare(62, legalMovesOpponent).isEmpty() &&
                    rookSquareShort.getPiece().getType() == ROOK) { // #26 @15:20 getPiece().isRook()
                    if (rookSquareShort.isOccupied() &&
                        rookSquareShort.getPiece().isFirstMove()) {
                        castlingMoves.add(new CastlingShortMove(this.board,
                                                                this.king,
                                                                62,
                                                                (Rook) rookSquareShort.getPiece(),
                                                                rookSquareShort.getPosition(), // current rook position
                                                                61));
                    }
                }
            }

            // CASTLING LONG "QUEENSIDE"
            if (!this.board.getSquare(59).isOccupied() &&
                !this.board.getSquare(58).isOccupied() &&
                !this.board.getSquare(57).isOccupied()) {
                final Square rookSquareLong = this.board.getSquare(56);
                if (Player.getCaptureMovesOnSquare(59, legalMovesOpponent).isEmpty() &&
                    Player.getCaptureMovesOnSquare(58, legalMovesOpponent).isEmpty() &&
                    Player.getCaptureMovesOnSquare(57, legalMovesOpponent).isEmpty() &&
                    rookSquareLong.getPiece().getType() == ROOK) {
                    if (rookSquareLong.isOccupied() &&
                        rookSquareLong.getPiece().isFirstMove()) {
                        castlingMoves.add(new CastlingLongMove(this.board,
                                                               this.king,
                                                               58,
                                                               (Rook) rookSquareLong.getPiece(),
                                                               rookSquareLong.getPosition(), // current Rook position
                                                               59));
                    }
                }
            }
        }

        return ImmutableList.copyOf(castlingMoves);
    }

}
