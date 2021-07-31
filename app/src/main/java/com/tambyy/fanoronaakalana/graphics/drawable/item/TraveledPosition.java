package com.tambyy.fanoronaakalana.graphics.drawable.item;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.tambyy.fanoronaakalana.engine.Engine.Point;
import com.tambyy.fanoronaakalana.graphics.drawable.Drawable;

public class TraveledPosition extends Drawable {

    private final Point position;

    /**
     * We use an anti-alias paint for a better rendering
     */
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public TraveledPosition(Point position) {
        this.position = position;
    }

    public void setColor(int color) {
        this.paint.setColor(color);
    }

    public void setOpacity(int opacity) {
        paint.setAlpha(opacity);
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
}
