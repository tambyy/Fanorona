package com.tambyy.fanoronaakalana.graphics.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

public class AkalanaLinearLayout extends RelativeLayout {
    public AkalanaLinearLayout(Context context) {
        super(context);
    }

    public AkalanaLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AkalanaLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AkalanaLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int unitSize = Math.min(
                widthMeasureSpec / 13,
                heightMeasureSpec / 5
        );

        super.setMeasuredDimension(unitSize * 13, unitSize * 5);
    }

}
