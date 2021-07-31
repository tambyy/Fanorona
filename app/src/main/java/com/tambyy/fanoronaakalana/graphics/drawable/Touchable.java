package com.tambyy.fanoronaakalana.graphics.drawable;

import java.util.ArrayList;
import java.util.List;

public abstract class Touchable extends Drawable {

    /**
     * Allow to touch the drawable
     */
    private boolean touchable = true;

    /**
     * Touch listeners
     */
    private final List<DrawableTouchListener> touchListeners = new ArrayList<>();

    /**
     *
     * @return touchable
     */
    public boolean isTouchable() {
        return touchable && !touchListeners.isEmpty();
    }

    /**
     *
     * @param touchable
     */
    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    /**
     * Add touch listener
     */
    public void addTouchListener(DrawableTouchListener listener) {
        touchListeners.add(listener);
    }

    /**
     *
     */
    public void touchStart() {
        for (DrawableTouchListener listener: touchListeners) {
            listener.onTouchStart(this);
        }
    }

    /**
     *
     */
    public void touchMove() {
        for (DrawableTouchListener listener: touchListeners) {
            listener.onTouchMove(this);
        }
    }

    /**
     *
     */
    public void touchUp() {
        for (DrawableTouchListener listener: touchListeners) {
            listener.onTouchUp(this);
        }
    }

    /**
     *
     */
    public void touchOver() {
        for (DrawableTouchListener listener: touchListeners) {
            listener.onTouchOver(this);
        }
    }

    /**
     *
     */
    public void touchOut() {
        for (DrawableTouchListener listener: touchListeners) {
            listener.onTouchOut(this);
        }
    }

    public List<DrawableTouchListener> getTouchListeners() {
        return touchListeners;
    }

    public abstract boolean bound(int x, int y);
}
