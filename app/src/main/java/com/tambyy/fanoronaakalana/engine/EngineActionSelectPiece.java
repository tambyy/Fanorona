package com.tambyy.fanoronaakalana.engine;

public class EngineActionSelectPiece extends EngineAction {

    private final Engine.Point position;

    public EngineActionSelectPiece(boolean black, Engine.Point position) {
        this.black = black;
        this.position = position;
    }

    public Engine.Point getPosition() {
        return position;
    }

    @Override
    public void applyToEngine(Engine engineManager) {
        engineManager.selectPiece(position.x, position.y);
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
