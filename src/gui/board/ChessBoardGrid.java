package gui.board;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

import static engine.board.BoardUtilities.SQUARES_ON_COL;
import static engine.board.BoardUtilities.SQUARES_ON_ROW;

public class ChessBoardGrid extends GridPane {

    public ChessBoardGrid() {
        setPrefSize(600,600);
        final List<SquarePane> boardSquares = new ArrayList<>(64);
        int pos = 0;
        for (int col = 0; col < SQUARES_ON_COL; col++) {
            for (int row = 0; row < SQUARES_ON_ROW; row++) {
                pos++;
                SquarePane boardSquare = new SquarePane();
                boardSquares.add(boardSquare); // add square pane to list of panes
                this.add(boardSquare, col, row);
                System.out.println("square: " + col + "," + row + " = " + (pos));
            }
        }
    }

    // INNER CLASS!
    public class SquarePane extends StackPane {
        // TODO: implement by adding piece graphics to stack?
    }

    // INNER CLASS!
    public class PieceGraphics extends Canvas {
        // TODO: implement movable piece graphics with canvas, or more suitable class?
    }

}
