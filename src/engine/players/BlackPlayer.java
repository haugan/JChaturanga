package engine.players;

import engine.board.Board;
import engine.moves.Move;
import engine.pieces.Piece;

import java.util.Collection;

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
    public Player getOpponent() {return this.board.getWhitePlayer() ;}

}
