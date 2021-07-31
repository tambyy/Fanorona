package com.tambyy.fanoronaakalana.graphics.anim.item;

import android.util.Log;

import com.tambyy.fanoronaakalana.graphics.anim.DrawableAnimation;
import com.tambyy.fanoronaakalana.graphics.anim.interpolator.Interpolator;
import com.tambyy.fanoronaakalana.graphics.drawable.Drawable;

public class DrawableMoveAnimation extends DrawableAnimation {

    int fromX, fromY, toX, toY;

    public DrawableMoveAnimation(Interpolator interpolator) {
        super(interpolator);
    }

    public void setFromTo(int fromX, int fromY, int toX, int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    @Override
    public void advance(double y) {
        int
            a = (int) (y * (toX - fromX) + fromX),
            b = (int) (y * (toY - fromY) + fromY);

        drawable.setPos(a, b);
    }
}
