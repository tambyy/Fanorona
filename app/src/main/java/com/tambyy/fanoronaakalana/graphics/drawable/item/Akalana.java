package com.tambyy.fanoronaakalana.graphics.drawable.item;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.tambyy.fanoronaakalana.graphics.drawable.Drawable;

public class Akalana extends Drawable {

    private final Path path = new Path();

    private Bitmap bitmap = null;
    private Bitmap scaledBitmap = null;

    public Akalana() {
        this.setColor(Color.rgb(230, 230, 230));
        this.paint.setStyle(Paint.Style.STROKE);
        this.setLineWidth(4);
    }

    public void setColor(int color) {
        this.paint.setColor(color);
    }

    public void setLineWidth(int lineWidth) {
        this.paint.setStrokeWidth(lineWidth);
    }

    @Override
    public void draw(Canvas canvas) {
        if (scaledBitmap != null) {
            canvas.drawBitmap(scaledBitmap, 0, 0, null);
        } else {
            canvas.drawPath(path, paint);
        }
    }

    public void setSize(int width, int height) {
        this.setWidth(width);
        this.setHeight(height);

        updateScaledBitmap();
        rewindPath();
    }

    /**
     *
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        updateScaledBitmap();
    }

    /**
     * Recreate scaled bitmap
     */
    private void updateScaledBitmap() {
        if (bitmap != null && width > 0 && height > 0) {
            scaledBitmap   = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }
    }

    /**
     *
     */
    private void rewindPath() {
        float unitSize = width / 9f;
        float unitSize2 = unitSize / 2f;

        path.rewind();

        // vertical
        for (int i = 0; i < 9; ++i) {
            float x1 = unitSize * i + unitSize2;
            float y1 = unitSize2;
            float y2 = y1 + unitSize * 4;

            path.moveTo(x1, y1);
            path.lineTo(x1, y2);
        }

        // horizontal
        for (int i = 0; i < 5; ++i) {
            float x1 = unitSize2;
            float y1 = unitSize * i + unitSize2;
            float x2 = x1 + unitSize * 8;

            path.moveTo(x1, y1);
            path.lineTo(x2, y1);
        }

        // oblique right
        path.moveTo(unitSize2, unitSize2 + 2 * unitSize);
        path.lineTo(unitSize2 + 2 * unitSize, unitSize2 + 4 * unitSize);

        path.moveTo(unitSize2, unitSize2);
        path.lineTo(unitSize2 + 4 * unitSize, unitSize2 + 4 * unitSize);

        path.moveTo(unitSize2 + 2 * unitSize, unitSize2);
        path.lineTo(unitSize2 + 6 * unitSize, unitSize2 + 4 * unitSize);

        path.moveTo(unitSize2 + 4 * unitSize, unitSize2);
        path.lineTo(unitSize2 + 8 * unitSize, unitSize2 + 4 * unitSize);

        path.moveTo(unitSize2 + 6 * unitSize, unitSize2);
        path.lineTo(unitSize2 + 8 * unitSize, unitSize2 + 2 * unitSize);


        // oblique left
        path.moveTo(unitSize2 + 2 * unitSize, unitSize2);
        path.lineTo(unitSize2, unitSize2 + 2 * unitSize);

        path.moveTo(unitSize2 + 4 * unitSize, unitSize2);
        path.lineTo(unitSize2, unitSize2 + 4 * unitSize);

        path.moveTo(unitSize2 + 6 * unitSize, unitSize2);
        path.lineTo(unitSize2 + 2 * unitSize, unitSize2 + 4 * unitSize);

        path.moveTo(unitSize2 + 8 * unitSize, unitSize2);
        path.lineTo(unitSize2 + 4 * unitSize, unitSize2 + 4 * unitSize);

        path.moveTo(unitSize2 + 8 * unitSize, unitSize2 + 2 * unitSize);
        path.lineTo(unitSize2 + 6 * unitSize, unitSize2 + 4 * unitSize);

    }
}
