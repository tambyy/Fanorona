package com.tambyy.fanoronaakalana.graphics.anim.item;

import android.util.Log;

import com.tambyy.fanoronaakalana.graphics.anim.DrawableAnimation;
import com.tambyy.fanoronaakalana.graphics.anim.interpolator.Interpolator;

public class DrawableScaleAnimation extends DrawableAnimation {

    float from, to;

    public DrawableScaleAnimation(Interpolator interpolator) {
        super(interpolator);
    }

    public void setFromTo(float from, float to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void advance(double y) {
        float scale = (float) (y * (to - from) + from);

        drawable.setScale(scale);
    }
}
