package engine.players;

public enum PlayerColor {

    BLACK {
        @Override
        public Player setPlayer(final WhitePlayer white, final BlackPlayer black) {
            return black;
        }
        @Override
        public int getMoveDirection() {return 1;} // counting "downwards" (63 is bottom-right)
        @Override
        public boolean isBlack() {return true;}
        @Override
        public boolean isWhite() {return false;}
        @Override
        public String toString() {return "b";}
    },

    WHITE {
        @Override
        public Player setPlayer(final WhitePlayer white, final BlackPlayer black) {
            return white;
        }
        @Override
        public int getMoveDirection() {return -1;} // counting "upwards" (0 is top-left)
        @Override
        public boolean isBlack() {return false;}
        @Override
        public boolean isWhite() {return true;}
        @Override
        public String toString() {return "w";}
    };


    public abstract Player setPlayer(final WhitePlayer white, final BlackPlayer black);
    public abstract int getMoveDirection();
    public abstract boolean isBlack();
    public abstract boolean isWhite();
}