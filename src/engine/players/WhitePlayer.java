package engine.players;

import engine.board.Board;
import engine.moves.Move;
import engine.pieces.Piece;

import java.util.Collection;

import static engine.players.PlayerColor.*;

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

}
