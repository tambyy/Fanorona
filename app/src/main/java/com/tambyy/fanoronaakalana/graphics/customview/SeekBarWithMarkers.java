package com.tambyy.fanoronaakalana.graphics.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SeekBarWithMarkers extends androidx.appcompat.widget.AppCompatSeekBar {
    Paint paint = new Paint();
    private int[] markersPosition = null;

    public SeekBarWithMarkers(@NonNull Context context) {
        super(context);
    }

    public SeekBarWithMarkers(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarWithMarkers(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMarkersPosition(int[] markersPosition) {
        this.markersPosition = markersPosition;
    }

    public void setMarkersColor(int color) {
        paint.setColor(color);
    }

    @Override
    protected synchronized void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (null != markersPosition && 0 != markersPosition.length) {
            final float width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            final float height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
            final float step = width / (float) getMax();

            // draw dots if we have ones
            for (int position : markersPosition) {
                canvas.drawRect(getPaddingLeft() + position * step - 1, height / 2 - 5, getPaddingLeft() + position * step + 1, height / 2 + 3, paint);
            }
        }
    }
}
