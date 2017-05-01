package engine.moves;

import engine.board.Board;

public class MoveTransaction {

    private final Board board;
    private final Move move;
    private final MoveTransactionResult result;

    /**
     * @param board TODO: comment this!
     * @param move TODO: comment this!
     * @param result TODO: comment this!
     */
    public MoveTransaction(final Board board,
                           final Move move,
                           final MoveTransactionResult result) {
        this.board = board;
        this.move = move;
        this.result = result;
    }

    /**
     * @return TODO: comment this!
     */
    public MoveTransactionResult getResult() {
        return this.result;
    }

}
