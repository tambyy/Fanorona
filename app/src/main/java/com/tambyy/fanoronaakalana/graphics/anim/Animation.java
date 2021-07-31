package com.tambyy.fanoronaakalana.graphics.anim;

import com.tambyy.fanoronaakalana.graphics.anim.interpolator.Interpolator;

public abstract class Animation {

    /**
     * Interpolator function
     */
    final Interpolator interpolator;

    /**
     *
     */
    int waitingDelay;

    /**
     * Animation delay in ms
     */
    int delay;

    /**
     * Animation x step
     */
    int timeStep;

    /**
     *
     */
    int waitingTimeStep;

    public int n;

    public Animation(Interpolator interpolator) {
        this.interpolator = interpolator;
        init();
    }

    public void init() {
        waitingDelay = 0;
        timeStep = 0;
        waitingTimeStep = 0;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setWaitingDelay(int waitingDelay) {
        this.waitingDelay = waitingDelay;
    }

    /**
     * Advance animation
     * @return
     *     animation not finished
     */
    public boolean advance(int timeIncrementation) {
        if (waitingTimeStep < waitingDelay) {
            waitingTimeStep += timeIncrementation;

            timeIncrementation = waitingTimeStep - waitingDelay;

            if (timeIncrementation <= 0) {
                return true;
            }
        }

        n = timeStep + timeIncrementation;

        // new time step value
        timeStep = Math.min(n, delay);

        advance(interpolator.f((double) timeStep / (double) delay));

        return n < delay;
    }

    /**
     *
     *
     * @param y
     */
    public abstract void advance(double y);

    /**
     *
     */
    private final java.util.List<AnimationDelayExceededListener> animationDelayExceededListeners = new java.util.ArrayList<AnimationDelayExceededListener>();

    /**
     * @param listener
     *
     */
    public final void addDelayExceededListener(AnimationDelayExceededListener listener) {
        animationDelayExceededListeners.add(listener);
    }

    /**
     *
     * @return
     */
    public final java.util.List<AnimationDelayExceededListener> getAnimationDelayExceededListeners() {
        return animationDelayExceededListeners;
    }

}
