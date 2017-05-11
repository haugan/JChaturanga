package engine.pieces;

import com.google.common.collect.ImmutableTable.Builder;
import com.google.common.collect.Table;
import engine.board.BoardUtilities;
import engine.moves.Move;
import engine.players.PlayerColor;

import static com.google.common.collect.ImmutableTable.builder;

public enum PieceUtilities {

    INSTANCE;

    private final Table<PlayerColor, Integer, Queen> POTENTIAL_QUEENS = PieceUtilities.getPotentialQueenPositions();
    private final Table<PlayerColor, Integer, Rook> POTENTIAL_ROOKS = PieceUtilities.getPotentialRookPositions();
    private final Table<PlayerColor, Integer, Knight> POTENTIAL_KNIGHTS = PieceUtilities.getPotentialKnightPositions();
    private final Table<PlayerColor, Integer, Bishop> POTENTIAL_BISHOPS = PieceUtilities.getPotentialBishopPositions();
    private final Table<PlayerColor, Integer, Pawn> POTENTIAL_PAWNS = PieceUtilities.getPotentialPawnPositions();

    Pawn getMovedPawn(final Move move) {
        return POTENTIAL_PAWNS.get(move.getMovedPiece().getColor(), move.getDestPos());
    }

    Knight getMovedKnight(final Move move) {
        return POTENTIAL_KNIGHTS.get(move.getMovedPiece().getColor(), move.getDestPos());
    }

    Bishop getMovedBishop(final Move move) {
        return POTENTIAL_BISHOPS.get(move.getMovedPiece().getColor(), move.getDestPos());
    }

    Rook getMovedRook(final Move move) {
        return POTENTIAL_ROOKS.get(move.getMovedPiece().getColor(), move.getDestPos());
    }

    Queen getMovedQueen(final Move move) {
        return POTENTIAL_QUEENS.get(move.getMovedPiece().getColor(), move.getDestPos());
    }

    private static Table<PlayerColor, Integer, Pawn> getPotentialPawnPositions() {

        final Builder<PlayerColor, Integer, Pawn> pawnsTable = builder();
        for(final PlayerColor color : PlayerColor.values()) {
            for(int pos = 0; pos < BoardUtilities.SQUARES_ON_BOARD; pos++) {
                pawnsTable.put(color, pos, new Pawn(pos, color, false));
            }
        }

        return pawnsTable.build();
    }

    private static Table<PlayerColor, Integer, Knight> getPotentialKnightPositions() {

        final Builder<PlayerColor, Integer, Knight> knightsTable = builder();
        for(final PlayerColor color : PlayerColor.values()) {
            for(int pos = 0; pos < BoardUtilities.SQUARES_ON_BOARD; pos++) {
                knightsTable.put(color, pos, new Knight(pos, color, false));
            }
        }

        return knightsTable.build();
    }

    private static Table<PlayerColor, Integer, Bishop> getPotentialBishopPositions() {

        final Builder<PlayerColor, Integer, Bishop> bishopsTable = builder();
        for(final PlayerColor color : PlayerColor.values()) {
            for(int pos = 0; pos < BoardUtilities.SQUARES_ON_BOARD; pos++) {
                bishopsTable.put(color, pos, new Bishop(pos, color, false));
            }
        }

        return bishopsTable.build();
    }

    private static Table<PlayerColor, Integer, Rook> getPotentialRookPositions() {

        final Builder<PlayerColor, Integer, Rook> rooksTable = builder();
        for(final PlayerColor color : PlayerColor.values()) {
            for(int pos = 0; pos < BoardUtilities.SQUARES_ON_BOARD; pos++) {
                rooksTable.put(color, pos, new Rook(pos, color, false));
            }
        }

        return rooksTable.build();
    }

    private static Table<PlayerColor, Integer, Queen> getPotentialQueenPositions() {

        final Builder<PlayerColor, Integer, Queen> queensTable = builder();
        for(final PlayerColor color : PlayerColor.values()) {
            for(int pos = 0; pos < BoardUtilities.SQUARES_ON_BOARD; pos++) {
                queensTable.put(color, pos, new Queen(pos, color, false));
            }
        }

        return queensTable.build();
    }

}
