package engine.moves;

public enum MoveTransactionResult {

    COMPLETED {
        @Override
        public boolean isCompleted() {
            return true;
        }
    },

    CANCELED {
        @Override
        public boolean isCompleted() {
            return false;
        }
    },

    PLAYER_CHECKED {
        @Override
        public boolean isCompleted() {
            return false;
        }
    };

    public abstract boolean isCompleted();

}
