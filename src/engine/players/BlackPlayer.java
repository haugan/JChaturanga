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
        if (this.king.isFirstMove() && !this.isChecked()) {
            // CASTLING SHORT
            if (!this.board.getSquare(5).isOccupied() &&
                !this.board.getSquare(6).isOccupied()) {
                final Square kingSideRookSquare = this.board.getSquare(7);
                if (Player.getCaptureMovesOnSquare(5, legalMovesOpponent).isEmpty() &&
                    Player.getCaptureMovesOnSquare(6, legalMovesOpponent).isEmpty() &&
                    kingSideRookSquare.getPiece().getType() == ROOK) { // #26 @15:20 getPiece().isRook()
                    if (kingSideRookSquare.isOccupied() &&
                        kingSideRookSquare.getPiece().isFirstMove()) {
                        castlingMoves.add(null); // TODO: add King-side castling move (i.e. short)
                    }
                }
            }
            // CASTLING LONG
            if (!this.board.getSquare(1).isOccupied() &&
                !this.board.getSquare(2).isOccupied() &&
                !this.board.getSquare(3).isOccupied()) {
                final Square queenSideRookSquare = this.board.getSquare(0);
                if (Player.getCaptureMovesOnSquare(1, legalMovesOpponent).isEmpty() &&
                    Player.getCaptureMovesOnSquare(2, legalMovesOpponent).isEmpty() &&
                    Player.getCaptureMovesOnSquare(3, legalMovesOpponent).isEmpty() &&
                    queenSideRookSquare.getPiece().getType() == ROOK) {
                    if (queenSideRookSquare.isOccupied() &&
                        queenSideRookSquare.getPiece().isFirstMove()) {
                        castlingMoves.add(null); // TODO: add Queen-side castling move (i.e. long)
                    }
                }
            }
        }
        return ImmutableList.copyOf(castlingMoves);
    }

}
