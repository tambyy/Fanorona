package com.tambyy.fanoronaakalana.graphics.anim;

import com.tambyy.fanoronaakalana.graphics.anim.Animation;
import com.tambyy.fanoronaakalana.graphics.anim.interpolator.Interpolator;
import com.tambyy.fanoronaakalana.graphics.drawable.Drawable;

public abstract class DrawableAnimation extends Animation {
    protected Drawable drawable;

    public DrawableAnimation(Interpolator interpolator) {
        super(interpolator);
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
