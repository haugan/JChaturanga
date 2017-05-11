package engine.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BoardUtilities {

    INSTANCE;

    public final List<String> PGN_NOTATION = initializePGNArray(); // containing all PGN notations
    public final Map<String, Integer> PGN_TO_POSITION = initializePGNMap(); // convert PGN string to Square squarePos
    public static final int FIRST_SQUARE = 0;
    public static final int SQUARES_ON_BOARD = 64; // number of Squares on chess Board
    public static final int SQUARES_ON_ROW = SQUARES_ON_BOARD / 8; // number of Squares on each row
    public static final int SQUARES_ON_COL = SQUARES_ON_BOARD / 8; // number of Squares on each column
    public static final List<Boolean> COLUMN_A = initCol(0); // first file (column) of Board
    public static final List<Boolean> COLUMN_B = initCol(1); // second file..
    public static final List<Boolean> COLUMN_C = initCol(2); // third file..
    public static final List<Boolean> COLUMN_D = initCol(3); // fourth file..
    public static final List<Boolean> COLUMN_E = initCol(4); // fifth file..
    public static final List<Boolean> COLUMN_F = initCol(5); // sixth file..
    public static final List<Boolean> COLUMN_G = initCol(6); // seventh file..
    public static final List<Boolean> COLUMN_H = initCol(7); // eighth file..
    public static final List<Boolean> ROW_8 = initRow(0);  // eighth rank (row) of Board
    public static final List<Boolean> ROW_7 = initRow(8);  // seventh rank..
    public static final List<Boolean> ROW_6 = initRow(16); // sixth rank..
    public static final List<Boolean> ROW_5 = initRow(24); // fifth rank..
    public static final List<Boolean> ROW_4 = initRow(32); // fourth rank..
    public static final List<Boolean> ROW_3 = initRow(40); // third rank..
    public static final List<Boolean> ROW_2 = initRow(48); // second rank..
    public static final List<Boolean> ROW_1 = initRow(56); // first rank..

    /**
     * @param colNumber representing each file (column) on the Board (e.g. 0/a, 1/b, etc).
     * @return a list of booleans where only the index numbers representing each column is set to true.
     */
    public static List<Boolean> initCol(int colNumber) {
        final Boolean[] cols = new Boolean[SQUARES_ON_BOARD];

        for(int i = 0; i < cols.length; i++) {
            cols[i] = false;
        }
        do {
            cols[colNumber] = true;
            colNumber += SQUARES_ON_COL;
        } while (colNumber < SQUARES_ON_BOARD);

        return ImmutableList.copyOf(cols);

    }

    /**
     * @param squarePos position at which the row begins (counting from top-left to bottom-right; 0 to 63).
     * @return a list of booleans where only the index numbers representing each row is set to true.
     */
    private static List<Boolean> initRow(int squarePos) {
        final Boolean[] rows = new Boolean[SQUARES_ON_BOARD];

        for(int i = 0; i < rows.length; i++) {
            rows[i] = false;
        }
        do {
            rows[squarePos] = true;
            squarePos++;
        } while (squarePos % SQUARES_ON_ROW != 0);

        return ImmutableList.copyOf(rows);
    }

    /**
     * @param squarePos position of Square to check for validity.
     * @return true (if Square is "on-board"; located at a square position between 0 and 63).
     */
    public static boolean isSquareOnBoard(final int squarePos) {
        return squarePos >= FIRST_SQUARE && squarePos < SQUARES_ON_BOARD;
    }

    // TODO: implement usage of these two methods for deciding checkmate or check situations
    public static boolean isGameCheckmated(final Board board) {
        return board.getCurrPlayer().isCheckmated() || board.getCurrPlayer().isCheckmated();
    }
    public static boolean isAnyPlayerChecked(final Board board) {
        return board.getWhitePlayer().isChecked() || board.getBlackPlayer().isChecked();
    }

    // TODO: implement usage, or remove these methods
    private static List<String> initializePGNArray() {
        return ImmutableList.copyOf(new String[]{
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        });
    }
    private Map<String, Integer> initializePGNMap() {
        final Map<String, Integer> positionPGNMap = new HashMap<>();
        for (int i = FIRST_SQUARE; i < SQUARES_ON_BOARD; i++) {
            positionPGNMap.put(PGN_NOTATION.get(i), i);
        }
        return ImmutableMap.copyOf(positionPGNMap);
    }
    public int getPositionForPGN(final String PGN) {
        return PGN_TO_POSITION.get(PGN);
    }
    public String getPGNFromPos(final int squarePosition) {
        return PGN_NOTATION.get(squarePosition);
    }

}
