package com.tambyy.fanoronaakalana.graphics.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class SquaredFromWidthRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {
    public SquaredFromWidthRadioButton(@NonNull Context context) {
        super(context);
    }

    public SquaredFromWidthRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaredFromWidthRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}