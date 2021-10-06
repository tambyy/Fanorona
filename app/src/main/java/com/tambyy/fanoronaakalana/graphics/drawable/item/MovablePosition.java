package com.tambyy.fanoronaakalana.graphics.drawable.item;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.tambyy.fanoronaakalana.engine.Engine.Move;
import com.tambyy.fanoronaakalana.engine.Engine.Point;
import com.tambyy.fanoronaakalana.graphics.drawable.Touchable;

public class MovablePosition extends Touchable {

    /**
     * From positions
     */
    private final Point from;

    /**
     *
     */
    private final Move move;

    Bitmap bitmap = null;

    private float circleRatioSize = 0.5f;

    /**
     * Paint to paint stroke
     */
    private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public MovablePosition(Point from, Move move) {
        this.from = from;
        this.move = move;
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(0);
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

    /**
     *
     * @return
     */
    public Point getFrom() {
        return from;
    }

    /**
     *
     * @return
     */
    public Move getMove() {
        return move;
    }

    /**
     *
     * @param color
     */
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    /**
     *
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     *
     * @param color
     */
    public void setStrokeColor(int color) {
        this.strokePaint.setColor(color);
    }

    /**
     *
     * @param width
     */
    public void setStrokeWidth(int width) {
        this.strokePaint.setStrokeWidth(width);
    }

    /**
     *
     * @param circleRatioSize
     */
    public void setCircleRatioSize(float circleRatioSize) {
        this.circleRatioSize = circleRatioSize;
    }

    @Override
    public void draw(Canvas canvas) {

        // if this bitmap is defined
        // we draw this
        if (bitmap != null) {

            canvas.drawBitmap(bitmap, 0, 0, paint);

            // we draw a default circle
            // with the default color
        } else {

            // circle size
            float size = circleRatioSize * width;

            // circle position
            // = (width - size) / 2
            // = (width - (4 * width / 9)) / 2
            float pos = (width - size) / 2f;

            RectF mRectF = new RectF(pos, pos, pos + size, pos + size);
            canvas.drawOval(mRectF, paint);
            canvas.drawOval(mRectF, strokePaint);
        }
    }

    @Override
    public boolean bound(int x, int y) {
        final int a = x - width / 2;
        final int b = y - height / 2;
        final int r = 2 * width / 3;

        return a * a + b * b <= r * r;
    }

}
