package com.tambyy.fanoronaakalana.engine;

import java.util.Arrays;

public class EngineActionInit extends EngineAction {
    private Engine.Point[] blackPositions;
    private Engine.Point[] whitePositions;
    private int mode;
    private boolean blackFirst;

    public EngineActionInit() {}

    public EngineActionInit(
            Engine.Point[] blackPositions,
            Engine.Point[] whitePositions,
            int mode,
            boolean blackFirst
    ) {
        this.blackPositions = blackPositions;
        this.whitePositions = whitePositions;
        this.mode = mode;
        this.blackFirst = blackFirst;
        this.black = blackFirst;
    }

    public Engine.Point[] getBlackPositions() {
        return blackPositions;
    }

    public void setBlackPositions(Engine.Point[] blackPositions) {
        this.blackPositions = blackPositions;
    }

    public Engine.Point[] getWhitePositions() {
        return whitePositions;
    }

    public void setWhitePositions(Engine.Point[] whitePositions) {
        this.whitePositions = whitePositions;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isBlackFirst() {
        return blackFirst;
    }

    public void setBlackFirst(boolean blackFirst) {
        this.blackFirst = blackFirst;
    }

    @Override
    public void applyToEngine(Engine engineManager) {
        engineManager.setPositions(blackPositions, whitePositions);
        engineManager.setMode(mode);
        engineManager.setFirstPlayerBlack(blackFirst);
    }

    @Override
    public String toString() {
        return "EngineActionInit{" +
                "A=" + Arrays.toString(blackPositions) +
                ", B=" + Arrays.toString(whitePositions) +
                ", V=" + mode +
                ", P=" + blackFirst +
                '}';
    }
}
