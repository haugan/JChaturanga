package engine.board;

public class BoardUtilities {

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

    private BoardUtilities() {
        throw new RuntimeException("The BoardUtilities class is not instantiable!");
    }

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

    /**
     * @param squarePosition of Square from top-left to bottom-right; 0 to 63.
     * @return true (if position is within bounds; thus valid).
     */
    public static boolean isValidSquarePosition(final int squarePosition) {
        return squarePosition >= 0 && squarePosition < SQUARES_ON_BOARD;
    }

}
