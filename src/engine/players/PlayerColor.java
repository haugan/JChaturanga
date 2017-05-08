package engine.players;

public enum PlayerColor {

    BLACK {
        @Override
        public Player setPlayer(final WhitePlayer white, final BlackPlayer black) {return black;}
        @Override
        public int getMoveDirection() {return 1;}
        @Override
        public boolean isBlack() {return true;}
        @Override
        public boolean isWhite() {return false;}
        @Override
        public String toString() {return "b";}
    },

    WHITE {
        @Override
        public Player setPlayer(final WhitePlayer white, final BlackPlayer black) {return white;}
        @Override
        public int getMoveDirection() {return -1;}
        @Override
        public boolean isBlack() {return false;}
        @Override
        public boolean isWhite() {return true;}
        @Override
        public String toString() {return "w";}
    };

    /**
     * TODO: comment this method
     * @param white TODO: comment this
     * @param black TODO: comment this
     * @return TODO: comment this
     */
    public abstract Player setPlayer(final WhitePlayer white, final BlackPlayer black);

    /**
     * @return negative or plus 1; counting the direction towards or from top-left Square of 0
     */
    public abstract int getMoveDirection();

    public abstract boolean isBlack();
    public abstract boolean isWhite();
}