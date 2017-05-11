package engine.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BoardUtilities {

    INSTANCE;

    public final List<String> PGN_NOTATION = initializePGNArray(); // containing all PGN notations
    public final Map<String, Integer> PGN_TO_POSITION = initializePGNMap(); // convert PGN string to Square pos
    public static final int FIRST_SQUARE_INDEX = 0;
    public static final int SQUARES_ON_BOARD = 64; // number of Squares on chess Board
    public static final int SQUARES_ON_ROW = SQUARES_ON_BOARD / 8; // number of Squares on each row
    public static final int SQUARES_ON_COL = SQUARES_ON_BOARD / 8; // number of Squares on each column
    public static final boolean[] COLUMN_A = initializeColumn(0); // first file (column) of Board
    public static final boolean[] COLUMN_B = initializeColumn(1); // second file..
    public static final boolean[] COLUMN_G = initializeColumn(6); // seventh file..
    public static final boolean[] COLUMN_H = initializeColumn(7); // eighth file..
    public static final boolean[] ROW_8 = initializeRow(0);  // eighth rank (row) of Board
    public static final boolean[] ROW_7 = initializeRow(8);  // seventh rank..
    public static final boolean[] ROW_6 = initializeRow(16); // sixth rank..
    public static final boolean[] ROW_5 = initializeRow(24); // fifth rank..
    public static final boolean[] ROW_4 = initializeRow(32); // fourth rank..
    public static final boolean[] ROW_3 = initializeRow(40); // third rank..
    public static final boolean[] ROW_2 = initializeRow(48); // second rank..
    public static final boolean[] ROW_1 = initializeRow(56); // first rank..

    /**
     * @param columnNumber representing each file (column) on the Board (e.g. 0/a, 1/b, etc).
     * @return array of booleans where only the index numbers representing each column is set to true.
     */
    public static boolean[] initializeColumn(int columnNumber) {
        final boolean[] columns = new boolean[64];
        do {
            columns[columnNumber] = true;
            columnNumber += SQUARES_ON_ROW;
        } while (
            columnNumber < SQUARES_ON_BOARD
        );
        return columns;
    }

    /**
     * @param squarePosition at which the row begins (counting from top-left to bottom-right; 0 to 63).
     * @return array of booleans where only the index numbers representing each row is set to true.
     */
    private static boolean[] initializeRow(int squarePosition) {
        final boolean[] rows = new boolean[SQUARES_ON_BOARD];
        do {
            rows[squarePosition] = true;
            squarePosition++;
        } while (
            squarePosition % SQUARES_ON_ROW != 0 // squarePosition < SQUARES_ON_ROW (?)
        );
        return rows;
    }

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
        for (int i = FIRST_SQUARE_INDEX; i < SQUARES_ON_BOARD; i++) {
            positionPGNMap.put(PGN_NOTATION.get(i), i);
        }
        return ImmutableMap.copyOf(positionPGNMap);
    }

    public static boolean isValidSquarePosition(final int pos) {
        return pos >= 0 && pos < SQUARES_ON_BOARD;
    }

    public int getPositionForPGN(final String PGN) {
        return PGN_TO_POSITION.get(PGN);
    } // TODO: find usages in BlackWidow source

    public String getPGNFromPos(final int squarePosition) {
        return PGN_NOTATION.get(squarePosition);
    } // TODO: find usages in BlackWidow source

}
