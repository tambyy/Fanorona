package com.tambyy.fanoronaakalana.engine;

public abstract class EngineAction {
    protected boolean black = true;
    protected long time = 0;

    public boolean isBlack() {
        return black;
    }

    public abstract void applyToEngine(Engine engineManager);

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
