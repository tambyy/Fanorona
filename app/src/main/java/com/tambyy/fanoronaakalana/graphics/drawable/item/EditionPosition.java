package com.tambyy.fanoronaakalana.graphics.drawable.item;

import android.graphics.Canvas;
import android.graphics.Color;

import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.graphics.drawable.Touchable;

public class EditionPosition extends Touchable {

    private final Engine.Point position;

    public EditionPosition(Engine.Point position) {
        this.position = position;
    }

    /**
     * Update width and height with the same value
     * and recreate scaled bitmaps after that
     * @param size
     */
    public void setSize(int size) {
        setWidth(size);
        setHeight(size);
    }

    public Engine.Point getPosition() {
        return position;
    }

    @Override
    public void draw(Canvas canvas) {}

    @Override
    public boolean bound(int x, int y) {
        final int a = x - width / 2;
        final int b = y - height / 2;
        final int r = width / 2;

        return a * a + b * b <= r * r;
    }

}