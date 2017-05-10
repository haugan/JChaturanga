package gui.board;

import engine.board.Board;
import engine.board.Square;
import engine.moves.Move;
import engine.moves.MoveTransaction;
import engine.pieces.Piece;
import gui.menu.MenuChoices;
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

import java.util.*;

import static engine.board.BoardUtilities.*;
import static engine.moves.Move.MoveFactory.createMove;
import static engine.moves.MoveTransactionResult.COMPLETED;
import static javafx.scene.input.MouseButton.PRIMARY;
import static javafx.scene.input.MouseButton.SECONDARY;
import static javafx.scene.paint.Color.*;

public class ChessBoardGrid extends GridPane implements Observer {

    private static boolean tooltipsEnabled = true; // showing tooltips in the GUI
    private static boolean legalMovesEnabled = true; // showing legal moves in the GUI
    private static final int BOARD_WIDTH = 600;
    private static final int BOARD_HEIGHT = 600;
    private static final int SQUARE_WIDTH = BOARD_WIDTH / 8;
    private static final int SQUARE_HEIGHT = BOARD_HEIGHT / 8;
    private static final Color LIGHT_COLOR = BLANCHEDALMOND;
    private static final Color DARK_COLOR = BURLYWOOD;
    private static final Color ACTIVE_COLOR = TOMATO;
    private static final Color HIGHLIGHT_COLOR = MEDIUMSPRINGGREEN;
    private List<SquareStack> squareStacks;
    private Board board; // initialized from Board class when "starting fresh"
    private Square squareSelected;
    private Square squareDestination;
    private Piece pieceSelected;

    public ChessBoardGrid() {
        setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);
        board = Board.initializeBoard();
        initializeGrid();
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((MenuChoices) arg) {
            case SHOW_TOOLTIP:
                tooltipsEnabled = !tooltipsEnabled;
                break;
            case SHOW_LEGAL_MOVES:
                legalMovesEnabled = !legalMovesEnabled;
                break;
        }
    }

    private void initializeGrid() {
        int squarePosition = 0;
        SquareStack squareStack;
        squareStacks = new ArrayList<>(64);

        for (int row = 0; row < SQUARES_ON_COL; row++) {
            for (int col = 0; col < SQUARES_ON_ROW; col++) {
                squareStack = new SquareStack(this, squarePosition, col, row, 0, 0); // square on board
                squareStacks.add(squareStack); // add initial SquareStacks to list of StackPanes
                add(squareStack, col, row); // add every SquareStack to this GridPane
                squarePosition++; // ID of SquareStack pane
            }
        }
    }

    private void redrawStacks(final Board board) {
        getChildren().clear();
        for (final SquareStack squareStack : squareStacks) {
            squareStack.drawGraphics(board, squareStack.position, 0, 0);
            getChildren().add(squareStack);
        }
        System.out.println("Stacks redrawn");
    }

    private void clearUserSelections() {
        squareSelected = null;
        squareDestination = null;
        pieceSelected = null;
        System.out.println("Selection canceled");
    }

    // INNER CLASS!
    public class SquareStack extends StackPane { // "stacking" background and piece graphics in one square

        private final int position; // numbered location of Stack on Grid from top-left to bottom-right; 0-63
        private SquareGraphic squareGraphic; // a square rectangle object giving background color to a board square
        private PieceGraphic pieceGraphic; // canvas object which holds image files for the chess pieces
        private Color bgColor;

        public SquareStack(final ChessBoardGrid grid, final int position,
                           final int col, final int row, final double xPos, final double yPos) {

            this.position = position;
            drawGraphics(board, position, xPos, yPos); // draw graphics using data from Board object

            // EVENT HANDLERS!
            setOnMouseEntered(event -> {
                FillTransition ft = new FillTransition(Duration.millis(100), squareGraphic, bgColor, ACTIVE_COLOR);
                ft.play();
                if (tooltipsEnabled) {
                    showTooltips(position, col, row); // show various info to user
                }
            }); // animate fill colors "in", call showTooltips()

            setOnMouseExited(event -> {
                FillTransition ft = new FillTransition(Duration.millis(100), squareGraphic, ACTIVE_COLOR, bgColor);
                ft.play();
            }); // animate fill colors "out"

            setOnMouseClicked(event -> {

                if (event.getButton() == SECONDARY) { // user clicks right mouse btn "cancel everything"
                    clearUserSelections();

                } else if (event.getButton() == PRIMARY) { // user clicks left mouse btn

                    if (squareSelected == null) { // no previous selections
                        squareSelected = board.getSquare(this.position);
                        pieceSelected = squareSelected.getPiece();

                        if (pieceSelected == null) {
                            squareSelected = null;
                        } else {
                            System.out.println("Piece selected (" + pieceSelected.toString() + ")");
                        }

                    } else {

                        squareDestination = board.getSquare(this.position);

                        // CREATE & EXECUTE MOVE!
                        final Move move = createMove(board,
                                                     squareSelected.getPosition(), squareDestination.getPosition());

                        final MoveTransaction transaction = board.getCurrentPlayer().performMove(move);

                        if (transaction.getResult() == COMPLETED) {
                            board = transaction.getBoard();
                            System.out.println("Move completed");
                        }

                        clearUserSelections(); // "reset" selected Square and Piece
                    }

                    // UPDATE GUI!
                    grid.redrawStacks(board);
                }
            }); // create and perform moves
        }

        private void drawGraphics(final Board board, final int position, final double xPos, final double yPos) {
            getChildren().clear(); // clear Board before drawing new

            squareGraphic = new SquareGraphic(xPos, yPos); // background graphics of "chess square"
            setSquareColors(position); // relative to Square's position on board (alternating light & dark)

            if (board.getSquare(position).isOccupied()) { // Square contains Piece
                String color = board.getSquare(position).getPiece().getColor().toString();
                String type = board.getSquare(position).getPiece().getType().toString();
                pieceGraphic = new PieceGraphic(color + type); // identifier references piece image filename
                getChildren().addAll(squareGraphic, pieceGraphic); // add "double node" to StackPane (i.e. occupied)
            } else {

                // TODO: refactor to method
                if (legalMovesEnabled && pieceSelected != null) {
                    highlightLegalMoves(board, position);
                }

                getChildren().add(squareGraphic); // add "single" node to StackPane (i.e. empty)
            }

        }

        private void setSquareColors(final int position) {
            if (ROW_8[position] || ROW_6[position] || ROW_4[position] || ROW_2[position]) {
                bgColor = (position % 2 == 0) ? LIGHT_COLOR : DARK_COLOR;
                squareGraphic.setFill(bgColor);
            }
            else if (ROW_7[position] || ROW_5[position] || ROW_3[position] || ROW_1[position]) {
                bgColor = (position % 2 != 0) ? LIGHT_COLOR : DARK_COLOR;
                squareGraphic.setFill(bgColor);
            }
        }

        private void showTooltips(int position, int col, int row) {
            Tooltip tooltip;
            Square square = board.getSquare(position);
            int pos = square.getPosition();
            if (square.isOccupied()) {
                Piece piece = square.getPiece();
                tooltip = new Tooltip(
                        "Position: " + pos + "\n"
                            + "Column: " + col + "\n"
                            + "Row: " + row + "\n"
                            + "Piece: " + piece.toString() + "\n"
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

        private void highlightLegalMoves(final Board board, final int position) {
            if (pieceSelected.getColor() == board.getCurrentPlayer().getColor()) {
                for (final Move legalMove : pieceSelected.calculateLegalMoves(board)) {
                    int destinationPosition = legalMove.getDestinationPosition();
                    if (destinationPosition == position) {
                        bgColor = HIGHLIGHT_COLOR;
                        squareGraphic.setFill(bgColor);
                    }
                }
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
