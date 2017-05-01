package application;

import engine.board.Board;

public class JavaSjakk {

    public static void main(String[] args) {
        Board b = Board.initializeBoard();
        System.out.println(b); // print ASCII board
    }

}
