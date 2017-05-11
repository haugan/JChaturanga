package engine.players;

import engine.board.BoardUtilities;

public enum PlayerColor {

    BLACK {
        @Override
        public Player setPlayer(final WhitePlayer white, final BlackPlayer black) {
            return black;
        }
        @Override
        public int getMoveDir() {return 1;} // counting "downwards" (63 is bottom-right)

        @Override
        public int getOppDir() {return DOWN_DIR;} // 1

        @Override
        public boolean isBlack() {return true;}
        @Override
        public boolean isWhite() {return false;}

        @Override
        public boolean hasReachedPromotion(final int squarePos) {return BoardUtilities.ROW_1.get(squarePos);}

        @Override
        public String toString() {return "b";}
    },

    WHITE {
        @Override
        public Player setPlayer(final WhitePlayer white, final BlackPlayer black) {
            return white;
        }
        @Override
        public int getMoveDir() {return -1;} // counting "upwards" (0 is top-left)

        @Override
        public int getOppDir() {return UP_DIR;} // -1

        @Override
        public boolean isBlack() {return false;}
        @Override
        public boolean isWhite() {return true;}

        @Override
        public boolean hasReachedPromotion(final int squarePos) {return BoardUtilities.ROW_8.get(squarePos);}

        @Override
        public String toString() {return "w";}
    };

    private static final int UP_DIR = -1; // upwards direction on board (i.e. towards square 0; top-left)
    private static final int DOWN_DIR = 1; // downwards direction on board (i.e. towards square 63; bot-right)

    public abstract Player setPlayer(final WhitePlayer white, final BlackPlayer black);
    public abstract int getMoveDir();
    public abstract int getOppDir(); // get opposite move direction
    public abstract boolean isBlack();
    public abstract boolean isWhite();
    public abstract boolean hasReachedPromotion(final int squarePos);
}