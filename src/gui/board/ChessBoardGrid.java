package gui.board;

import engine.board.Board;
import engine.board.Square;
import engine.pieces.Piece;
import javafx.animation.FillTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static engine.board.BoardUtilities.*;
import static javafx.scene.paint.Color.*;

public class ChessBoardGrid extends GridPane {

    public static boolean tooltipsEnabled = false;
    public static final int BOARD_WIDTH = 600;
    public static final int BOARD_HEIGHT = 600;
    public static final int SQUARE_WIDTH = BOARD_WIDTH / 8;
    public static final int SQUARE_HEIGHT = BOARD_HEIGHT / 8;
    public static final Color LIGHT_COLOR = BLANCHEDALMOND;
    public static final Color DARK_COLOR = BURLYWOOD;
    public static final Color ACTIVE_COLOR = TOMATO;
    private List<SquareStack> squareStacks;
    private Board board; // initialized from Board class when "starting fresh"

    public ChessBoardGrid() {
        setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);
        board = Board.initializeBoard();
        initializeGrid();
    }

    private void initializeGrid() {
        int squarePosition = 0;
        SquareStack squareStack;
        squareStacks = new ArrayList<>(64);

        for (int row = 0; row < SQUARES_ON_COL; row++) {
            for (int col = 0; col < SQUARES_ON_ROW; col++) {
                squareStack = new SquareStack(squarePosition, col, row, 0, 0); // square on board
                squareStacks.add(squareStack); // add initial SquareStacks to list of StackPanes
                add(squareStack, col, row); // add every SquareStack to this GridPane
                squarePosition++; // ID of SquareStack pane
            }
        }
    }

    // INNER CLASS!
    public class SquareStack extends StackPane { // "stacking" background and piece graphics in one square

        private SquareGraphic squareGraphic;
        private PieceGraphic pieceGraphic;
        private Color bgColor;

        public SquareStack(final int position, final int col, final int row, final double xPos, final double yPos) {

            drawGraphics(board, position, xPos, yPos); // draw graphics using data from Board object
            setSquareColors(position); // relative to Square's position on board (alternating light & dark)

            // EVENT HANDLERS!
            setOnMouseEntered(event -> {
                FillTransition ft = new FillTransition(Duration.millis(100), squareGraphic, bgColor, ACTIVE_COLOR);
                ft.play();
                showTooltips(position, col, row); // show various info to user
                //squareGraphic.setFill(ACTIVE_COLOR);
            }); // animate fill colors "in"
            setOnMouseExited(event -> {
                FillTransition ft = new FillTransition(Duration.millis(100), squareGraphic, ACTIVE_COLOR, bgColor);
                ft.play();
                //squareGraphic.setFill(bgColor);
            }); // animate fill colors "out"
            setOnMouseClicked(event -> {

            });
        }

        private void drawGraphics(final Board board, final int position, final double xPos, final double yPos) {
            getChildren().clear(); // clear Board before drawing new
            squareGraphic = new SquareGraphic(xPos, yPos); // background graphics of "chess square"
            if (board.getSquare(position).isOccupied()) { // Square contains Piece
                String color = board.getSquare(position).getPiece().getColor().toString();
                String type = board.getSquare(position).getPiece().getType().toString();
                pieceGraphic = new PieceGraphic(color + type); // identifier references piece image filename
                getChildren().addAll(squareGraphic, pieceGraphic); // add "double node" to StackPane (i.e. occupied)
            } else {
                getChildren().add(squareGraphic); // add "single" node to StackPane (i.e. empty)
            }
        }

        private void setSquareColors(final int position) {
            if (ROW_8[position] || ROW_6[position] || ROW_4[position] || ROW_2[position]) {
                bgColor = position % 2 == 0 ? LIGHT_COLOR : DARK_COLOR;
                squareGraphic.setFill(bgColor);
            }
            else if (ROW_7[position] || ROW_5[position] || ROW_3[position] || ROW_1[position]) {
                bgColor = position % 2 != 0 ? LIGHT_COLOR : DARK_COLOR;
                squareGraphic.setFill(bgColor);
            }
        }

        private void showTooltips(int position, int col, int row) {
            if (tooltipsEnabled) {
                Tooltip tooltip;
                Square square = board.getSquare(position);
                int pos = square.getPosition();
                if (square.isOccupied()) {
                    Piece piece = square.getPiece();
                    tooltip = new Tooltip(
                            "Position: " + pos + "\n"
                                    + "Column: " + col + "\n"
                                    + "Row: " + row + "\n"
                                    + "Piece: " + piece.toString().toUpperCase() + "\n"
                                    + "Color: " + piece.getColor().toString().toUpperCase()
                    );
                } else {
                    tooltip = new Tooltip(
                            "Position: " + pos + "\n"
                                    + "Column: " + col + "\n"
                                    + "Row: " + row
                    );
                }
                Tooltip.install(this, tooltip);
            }
        }

    }

    // INNER CLASS!
    public class SquareGraphic extends Rectangle {

        public SquareGraphic(final double xPos, final double yPos) {
            super(SQUARE_WIDTH, SQUARE_HEIGHT);
            setX(xPos); // position is relative to parent node (i.e. StackPane)
            setY(yPos); // position is relative to parent node (i.e. StackPane)
        }

    }

    // INNER CLASS!
    public class PieceGraphic extends Canvas {

        public PieceGraphic(final String identifier) {
            super(SQUARE_WIDTH, SQUARE_HEIGHT);
            GraphicsContext graphics = this.getGraphicsContext2D();
            String imgURL = "/res/img/" + identifier + ".png";
            Image img = new Image(imgURL);
            graphics.drawImage(img, 0, 0, SQUARE_WIDTH, SQUARE_HEIGHT);
        }

    }

}
