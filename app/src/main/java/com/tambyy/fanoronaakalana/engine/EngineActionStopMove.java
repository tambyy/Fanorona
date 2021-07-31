package com.tambyy.fanoronaakalana.engine;

import androidx.annotation.NonNull;

public class EngineActionStopMove extends EngineAction {

    public EngineActionStopMove(boolean black) {
        this.black = black;
    }

    @Override
    public void applyToEngine(Engine engineManager) {
        engineManager.stopMove();
    }

    @NonNull
    @Override
    public String toString() {
        return "STOP";
    }
}
