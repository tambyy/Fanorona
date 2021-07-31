package com.tambyy.fanoronaakalana.graphics.anim.interpolator;

public interface Interpolator {

    /**
     * @param x
     *     x value between 0 and 1
     * @return
     *     y value between 0 and 1
     */
    public double f(double x);
}
