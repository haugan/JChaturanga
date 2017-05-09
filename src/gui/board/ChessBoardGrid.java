package gui.board;

import engine.board.Board;
import engine.board.Square;
import engine.pieces.Piece;
import engine.players.PlayerColor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static engine.board.BoardUtilities.*;
import static javafx.scene.paint.Color.BLANCHEDALMOND;
import static javafx.scene.paint.Color.BURLYWOOD;
import static javafx.scene.paint.Color.TOMATO;

public class ChessBoardGrid extends GridPane {

    public static boolean hintsEnabled = false; // TODO: implement methods for showing legal moves
    public static final int BOARD_WIDTH = 600;
    public static final int BOARD_HEIGHT = 600;
    public static final int SQUARE_WIDTH = BOARD_WIDTH / 8;
    public static final int SQUARE_HEIGHT = BOARD_HEIGHT / 8;
    public static final Color LIGHT_SQUARE = BLANCHEDALMOND;
    public static final Color DARK_SQUARE = BURLYWOOD;
    public static final Color ACTIVE_SQUARE = TOMATO;
    private List<SquareStack> squareStacks;
    private Board board;

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
        private int position;
        private int col;
        private int row;

        public SquareStack(final int position, final int col, final int row, final double xPos, final double yPos) {

            this.position = position;
            this.col = col;
            this.row = row;

            squareGraphic = new SquareGraphic(xPos, yPos); // background graphics of "chess square"
            setBackgroundColor(); // relative to Square's position on board (alternating light & dark)
            setSquareGraphics(board);

            // EVENT HANDLERS!
            setOnMouseEntered(event -> squareGraphic.setFill(ACTIVE_SQUARE));
            setOnMouseExited(event -> squareGraphic.setFill(bgColor));
            setOnMouseClicked(event -> {
                Square square = board.getSquare(position);
                int squarePos = square.getPosition();
                Piece piece = square.getPiece();
                PlayerColor color = null;
                if (piece != null) {
                    color = piece.getColor();
                }
                System.out.println("square " + squarePos);
                System.out.println("column " + this.col);
                System.out.println("row " + this.row);
                if (piece != null) {
                    System.out.println("piece " + piece.toString().toUpperCase());
                }
                if (color != null) {
                    System.out.println("color " + color.toString().toUpperCase());
                }
            });
        }

        private void setBackgroundColor() {
            if (ROW_8[position] || ROW_6[position] || ROW_4[position] || ROW_2[position]) {
                bgColor = position % 2 == 0 ? LIGHT_SQUARE : DARK_SQUARE;
                squareGraphic.setFill(bgColor);
            }
            else if (ROW_7[position] || ROW_5[position] || ROW_3[position] || ROW_1[position]) {
                bgColor = position % 2 != 0 ? LIGHT_SQUARE : DARK_SQUARE;
                squareGraphic.setFill(bgColor);
            }
        }

        private void setSquareGraphics(final Board board) {
            getChildren().remove(pieceGraphic);
            if (board.getSquare(position).isOccupied()) { // Square contains Piece
                String color = board.getSquare(position).getPiece().getColor().toString();
                String type = board.getSquare(position).getPiece().getType().toString();
                pieceGraphic = new PieceGraphic(color + type); // identifier references filename of image
                getChildren().addAll(squareGraphic, pieceGraphic); // add stacked nodes to this StackPane
            } else {
                getChildren().add(squareGraphic); // add single node to this StackPane
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
