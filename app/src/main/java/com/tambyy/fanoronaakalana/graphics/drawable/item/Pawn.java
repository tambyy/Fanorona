package com.tambyy.fanoronaakalana.graphics.drawable.item;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.tambyy.fanoronaakalana.graphics.drawable.Touchable;

/**
 * This class instantiates a piece inside the akalanaView,
 * the piece can be represented by an image
 * or else by a color
 */
public class Pawn extends Touchable {

    /**
     * Different values of the state of the piece
     */
    public enum State {
        NONE,
        MOVABLE,
        SELECTED
    }

    /**
     *
     */
    private static final int defaultMovableColor = Color.argb(90, 255, 200, 60);

    /**
     * Paint to paint stroke
     */
    private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     *
     */
    private final boolean black;

    /**
     * State of the piece
     */
    private State state = State.NONE;

    /**
     * If defined,
     * we draw the following bitmap inside the canvas
     */
    Bitmap defaultBitmap = null;
    Bitmap movableBitmap = null;
    Bitmap selectedBitmap = null;

    private int fx = 0;
    private int fy = 0;

    private float circleRatioSize = 0.5f;

    public Pawn(boolean black) {
        this.black = black;
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(1.5f);
    }

    /**
     *
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     *
     * @param state
     */
    public void setState(State state) {
        this.state = state;
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

    public boolean isBlack() {
        return black;
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
     * @param fx
     * @param fy
     */
    public void setFPos(int fx, int fy) {
        this.fx = fx;
        this.fy = fy;
    }

    public int getFx() {
        return fx;
    }

    public int getFy() {
        return fy;
    }

    /**
     *
     * @param defaultBitmap
     * @param movableBitmap
     * @param selectedBitmap
     */
    public void setBitmaps(
        Bitmap defaultBitmap,
        Bitmap movableBitmap,
        Bitmap selectedBitmap
    ) {
        this.defaultBitmap   = defaultBitmap;
        this.movableBitmap   = movableBitmap;
        this.selectedBitmap  = selectedBitmap;
    }

    /**
     * Retrieve the bitmap to draw according to the state of the piece
     * (none, movable, selected)
     * @return
     */
    private Bitmap bitmapToDraw() {
        Bitmap bitmap;

        switch (state) {
            case MOVABLE:
                bitmap = movableBitmap;
                break;
            case SELECTED:
                bitmap = selectedBitmap;
                break;
            default:
                bitmap = defaultBitmap;
                break;
        }

        return bitmap != null ? bitmap : defaultBitmap;
    }

    @Override
    public void draw(Canvas canvas) {

        // what bitmap we should draw?
        final Bitmap bitmap = bitmapToDraw();

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

            // show movable pieces
            if (state != State.NONE) {
                final int color = paint.getColor();
                paint.setColor(defaultMovableColor);
                canvas.drawOval(mRectF, paint);
                paint.setColor(color);
            }
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
