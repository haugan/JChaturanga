package engine.moves;

import engine.board.Board;

public class MoveTransaction {

    private final Board board;
    private final Move move;
    private final MoveTransactionResult result;

    public MoveTransaction(final Board board,
                           final Move move,
                           final MoveTransactionResult result) {
        this.board = board;
        this.move = move;
        this.result = result;
    }

    public MoveTransactionResult getResult() {return this.result;}
    public Board getBoard() {return this.board;}

}
