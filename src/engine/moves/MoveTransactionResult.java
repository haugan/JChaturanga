package engine.moves;

public enum MoveTransactionResult {

    COMPLETED {
        @Override
        public boolean isMovePerformed() {return true;}
    },
    CANCELED {
        @Override
        public boolean isMovePerformed() {return false;}
    },
    PLAYER_CHECKED {
        @Override
        public boolean isMovePerformed() {return false;}
    };

    /**
     * @return TODO: comment this!
     */
    public abstract boolean isMovePerformed();

}
