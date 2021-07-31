package com.tambyy.fanoronaakalana.graphics.anim.item;

import com.tambyy.fanoronaakalana.graphics.anim.DrawableAnimation;
import com.tambyy.fanoronaakalana.graphics.anim.interpolator.Interpolator;

public class DrawableOpacityAnimation extends DrawableAnimation {

    int from, to;

    public DrawableOpacityAnimation(Interpolator interpolator) {
        super(interpolator);
    }

    public void setFromTo(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void advance(double y) {
        int opacity = (int) (y * (to - from) + from);

        drawable.setOpacity(opacity);
    }
}
