package engine.players;

import com.google.common.collect.ImmutableList;
import engine.board.Board;
import engine.board.Square;
import engine.moves.Move;
import engine.pieces.Piece;

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
            // CASTLING SHORT
            if (!this.board.getSquare(61).isOccupied() &&
                !this.board.getSquare(62).isOccupied()) {
                final Square kingSideRookSquare = this.board.getSquare(63);
                if (Player.getCaptureMovesOnSquare(61, legalMovesOpponent).isEmpty() &&
                    Player.getCaptureMovesOnSquare(62, legalMovesOpponent).isEmpty() &&
                    kingSideRookSquare.getPiece().getType() == ROOK) { // #26 @15:20 getPiece().isRook()
                    if (kingSideRookSquare.isOccupied() &&
                        kingSideRookSquare.getPiece().isFirstMove()) {
                        castlingMoves.add(null); // TODO: add King-side castling move (i.e. short)
                    }
                }
            }
            // CASTLING LONG
            if (!this.board.getSquare(59).isOccupied() &&
                !this.board.getSquare(58).isOccupied() &&
                !this.board.getSquare(57).isOccupied()) {
                final Square queenSideRookSquare = this.board.getSquare(56);
                if (queenSideRookSquare.isOccupied() &&
                    queenSideRookSquare.getPiece().isFirstMove()) {
                    castlingMoves.add(null); // TODO: add Queen-side castling move (i.e. long)
                }
            }
        }
        return ImmutableList.copyOf(castlingMoves);
    }

}
