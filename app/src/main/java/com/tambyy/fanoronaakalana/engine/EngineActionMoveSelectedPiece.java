package com.tambyy.fanoronaakalana.engine;

public class EngineActionMoveSelectedPiece extends EngineAction {

    private final Engine.Move move;

    public EngineActionMoveSelectedPiece(boolean black, Engine.Move move) {
        this.black = black;
        this.move = move;
    }

    public Engine.Move getMove() {
        return move;
    }

    @Override
    public void applyToEngine(Engine engineManager) {
        engineManager.movePiece(move.vector, move.percute);
    }

    @Override
    public String toString() {
        return move.toString();
    }
}
