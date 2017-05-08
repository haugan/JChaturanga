package gui.board;

import application.JChaturanga;
import engine.board.Board;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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

        // TODO: look into solutions for sharing board state between classes
        initializeGrid(JChaturanga.board);
    }

    private void initializeGrid(final Board board) {
        int squarePosition = 0;
        SquareStack boardSquare;
        final List<SquareStack> boardSquares = new ArrayList<>(64);

        for (int row = 0; row < SQUARES_ON_COL; row++) {
            for (int col = 0; col < SQUARES_ON_ROW; col++) {

                if ((row + col) % 2 == 0) {
                    boardSquare = new SquareStack(squarePosition,0,0, LIGHT_SQUARE, board);
                } else {
                    boardSquare = new SquareStack(squarePosition,0,0, DARK_SQUARE, board);
                }

                boardSquares.add(boardSquare); // add SquareStack to list of panes
                this.add(boardSquare, col, row); // add SquareStack to parent GridPane
                System.out.println("square" + squarePosition + " (col:" + col + ", row:" + row + ")");
                squarePosition++; // ID of SquareStack pane
            }
        }

    }

    // INNER CLASS!
    public class SquareStack extends StackPane {

        int squarePosition;
        double xPos, yPos;
        Color bgColor;
        SquareGraphic squareGraphic;
        PieceGraphic pieceGraphic;

        public SquareStack(int squarePosition, double xPos, double yPos, Color bgColor, Board board) {
            this.squarePosition = squarePosition;
            this.xPos = xPos;
            this.yPos = yPos;
            this.bgColor = bgColor;
            squareGraphic = new SquareGraphic(xPos, yPos, bgColor);

            if (board.getSquare(squarePosition).isOccupied()) { // Square contains Piece
                String color = board.getSquare(squarePosition).getPiece().getColor().toString();
                String type = board.getSquare(squarePosition).getPiece().getType().toString();
                pieceGraphic = new PieceGraphic(color + type); // identifier references filename of image
                this.getChildren().addAll(squareGraphic, pieceGraphic);
            } else {
                this.getChildren().add(squareGraphic);
            }

        }

    }

    // INNER CLASS!
    public class SquareGraphic extends Rectangle {

        public SquareGraphic(double xPos, double yPos, Color bgColor) {
            super(SQUARE_WIDTH, SQUARE_HEIGHT, bgColor);
            this.setX(xPos); // position is relative to parent node (i.e. StackPane)
            this.setY(yPos); // position is relative to parent node (i.e. StackPane)
        }

    }

    // INNER CLASS!
    public class PieceGraphic extends Canvas {

        public PieceGraphic(String identifier) {
            super(SQUARE_WIDTH, SQUARE_HEIGHT);
            GraphicsContext graphics = this.getGraphicsContext2D();
            String imgURL = "/res/img/" + identifier + ".png";
            Image img = new Image(imgURL);
            graphics.drawImage(img, 0, 0, SQUARE_WIDTH, SQUARE_HEIGHT);
            System.out.println("piece: " + identifier);
        }

    }

}
