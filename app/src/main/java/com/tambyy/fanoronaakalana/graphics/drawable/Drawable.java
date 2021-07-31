package com.tambyy.fanoronaakalana.graphics.drawable;

import android.graphics.Paint;

import com.tambyy.fanoronaakalana.graphics.customview.SceneView;

public abstract class Drawable {

    /**
     * We use an anti-alias paint for a better rendering
     */
    protected final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * x position
     */
    protected int x = 0;

    /**
     * y position
     */
    protected int y = 0;

    /**
     * z index
     */
    protected int z = 0;

    /**
     * width
     */
    protected int width = 0;

    /**
     * height
     */
    protected int height = 0;

    /**
     * allows to scale object size
     * value must be between 0 and 1f
     */
    protected float scale = 1f;

    /**
     *
     */
    protected SceneView parent;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setParent(SceneView parent) {
        this.parent = parent;
    }

    public void setOpacity(int opacity) {
        paint.setAlpha(opacity);
    }

    /**
     * Method to implement to draw the drawable
     * into a canvas given by a parent view
     */
    public abstract void draw(android.graphics.Canvas canvas);
}
