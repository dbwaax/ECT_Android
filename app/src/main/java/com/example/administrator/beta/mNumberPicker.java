package com.example.administrator.beta;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

public class mNumberPicker extends NumberPicker {
    public mNumberPicker(Context context) {
        super(context);
    }
    public mNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    public void updateView(View view) {
        if (view instanceof EditText) {
            //这里修改显示字体的属性，主要修改颜色
            ((EditText) view).setTextColor(Color.parseColor("#000000"));
            ((EditText) view).setTextSize(30);
        }
    }
}
