package com.yuqinyidev.android.azaz.kanbook.mvp.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class KBTextProgressBar extends ProgressBar {

    private String text;
    private Paint mPaint;

    public KBTextProgressBar(Context context) {
        super(context);
        System.out.println("1");
        initText();
    }

    public KBTextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        System.out.println("2");
        initText();
    }

    public KBTextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        System.out.println("3");
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        this.mPaint.setColor(Color.BLACK);
        canvas.drawText(this.text, x, y, this.mPaint);
    }

    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);
    }

    private void setText(int progress) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.applyPattern("###.##");
        this.text = df.format((progress * 100F) / Float.valueOf(this.getMax()))
                + "%";
    }
}
