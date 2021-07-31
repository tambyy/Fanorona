package com.tambyy.fanoronaakalana.graphics.anim.interpolator;

public class SecondDegreeInterpolator implements Interpolator {

    @Override
    public double f(double x) {
        return x * (2 - x);
    }
}
