package engine.moves;

import engine.board.Board;

public class MoveTransaction {

    private final Board board;
    private final Board newBoard;
    private final Move move;
    private final MoveTransactionResult result;

    public MoveTransaction(final Board board, final Board newBoard, final Move move, final MoveTransactionResult result) {
        this.board = board;
        this.newBoard = newBoard;
        this.move = move;
        this.result = result;
    }

    public MoveTransactionResult getResult() {return result;}
    public Board getBoard() {return board;}
    public Board getNewBoard() {return newBoard;}

}
