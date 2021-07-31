package com.tambyy.fanoronaakalana.graphics.drawable.item;

import android.graphics.Canvas;

import com.tambyy.fanoronaakalana.engine.Engine.Point;
import com.tambyy.fanoronaakalana.engine.Engine.Move;
import com.tambyy.fanoronaakalana.graphics.drawable.Touchable;

public class RemovablePawn extends Touchable {

    private final Point position;

    private final Move move;

    public RemovablePawn(Point position, Move move) {
        this.position = position;
        this.move = move;
    }

    public void setColor(int color) {
        this.paint.setColor(color);
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

    public Point getPosition() {
        return position;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public void draw(Canvas canvas) {
        // circle size
        float size = 4f * width / 9f;

        // circle position
        // = (width - size) / 2
        // = (width - (4 * width / 9)) / 2
        float pos = (width - size) / 2f;

        canvas.drawOval(pos, pos, pos + size, pos + size, paint);
    }

    @Override
    public boolean bound(int x, int y) {
        final int a = x - width / 2;
        final int b = y - height / 2;
        final int r = 2 * width / 3;

        return a * a + b * b <= r * r;
    }

}
