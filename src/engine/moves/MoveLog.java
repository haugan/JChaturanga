package engine.moves;

import java.util.ArrayList;
import java.util.List;

public class MoveLog {

    private static MoveLog MOVE_LOG_INSTANCE = null;
    private static List<Move> moveList;

    public MoveLog() {
        moveList = new ArrayList<>();
    }

    public static MoveLog getInstance() {
        if (MOVE_LOG_INSTANCE == null) {
            MOVE_LOG_INSTANCE = new MoveLog();
        }

        return MOVE_LOG_INSTANCE;
    }

    public static List<Move> getMoveList() {
        return moveList;
    }

    public void addMove(final Move move) {
        moveList.add(move);
    }

    @Override
    public String toString() {
        for (Move m : moveList) {
            return m.toString();
        }
        return "";
    }
}
