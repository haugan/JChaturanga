package gui.board;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static engine.board.BoardUtilities.SQUARES_ON_COL;
import static engine.board.BoardUtilities.SQUARES_ON_ROW;

public class ChessBoardGrid extends GridPane {

    public static final int BOARD_WIDTH = 600;
    public static final int BOARD_HEIGHT = 600;
    public static final int SQUARE_WIDTH = BOARD_WIDTH / 8;
    public static final int SQUARE_HEIGHT = BOARD_HEIGHT / 8;
    public static final Color LIGHT_SQUARE = Color.ALICEBLUE;
    public static final Color DARK_SQUARE = Color.CADETBLUE;

    public ChessBoardGrid() {
        setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);
        final List<SquareStack> boardSquares = new ArrayList<>(64);
        SquareStack boardSquare;
        for (int col = 0; col < SQUARES_ON_COL; col++) {
            for (int row = 0; row < SQUARES_ON_ROW; row++) {
                if ((col + row) % 2 == 0) {
                    boardSquare = new SquareStack(0,0, LIGHT_SQUARE);
                } else {
                    boardSquare = new SquareStack(0,0, DARK_SQUARE);
                }
                boardSquares.add(boardSquare); // add square stack to list of panes
                this.add(boardSquare, col, row); // add square stack to parent grid
                System.out.println("square: " + col + "," + row);
            }
        }
    }

    // INNER CLASS!
    public class SquareStack extends StackPane {

        double xPos, yPos;
        Color backgroundColor;
        SquareGraphic squareGraphic;
        PieceGraphic pieceGraphic;

        public SquareStack(double xPos, double yPos, Color backgroundColor) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.backgroundColor = backgroundColor;
            squareGraphic = new SquareGraphic(xPos, yPos, backgroundColor);
            pieceGraphic = new PieceGraphic();
            this.getChildren().addAll(squareGraphic, pieceGraphic);
        }

    }

    // INNER CLASS!
    public class SquareGraphic extends Rectangle {

        public SquareGraphic(double xPos, double yPos, Color backgroundColor) {
            super(SQUARE_WIDTH, SQUARE_HEIGHT, backgroundColor);
            this.setX(xPos); // position is relative to parent node (i.e. StackPane)
            this.setY(yPos); // position is relative to parent node (i.e. StackPane)
        }

    }

    // INNER CLASS!
    public class PieceGraphic extends Canvas {
        // TODO: nodes can be styled with CSS!
    }

}
