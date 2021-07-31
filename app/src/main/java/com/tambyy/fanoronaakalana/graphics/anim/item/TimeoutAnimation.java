package com.tambyy.fanoronaakalana.graphics.anim.item;

import com.tambyy.fanoronaakalana.graphics.anim.Animation;
import com.tambyy.fanoronaakalana.graphics.anim.AnimationDelayExceededListener;
import com.tambyy.fanoronaakalana.graphics.anim.interpolator.Interpolator;
import com.tambyy.fanoronaakalana.graphics.anim.interpolator.LinearInterpolator;
import com.tambyy.fanoronaakalana.graphics.drawable.Drawable;

public class TimeoutAnimation extends Animation {

    public TimeoutAnimation(int delay, AnimationDelayExceededListener listener) {
        super(new LinearInterpolator());
        setDelay(delay);
        addDelayExceededListener(listener);
    }

    @Override
    public void advance(double y) {}
}