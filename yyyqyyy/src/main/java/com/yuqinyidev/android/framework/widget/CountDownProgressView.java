package com.yuqinyidev.android.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.yuqinyidev.android.framework.R;

import java.security.PublicKey;

import timber.log.Timber;

/**
 * Created by RDX64 on 2017/7/2.
 */

public class CountDownProgressView extends TextView {

    private Runnable progressChangeTask = new Runnable() {
        @Override
        public void run() {
            removeCallbacks(this);
            switch (mProgressType) {
                case COUNT:
                    progress += 1;
                    break;
                case COUNT_BACK:
                    progress -= 1;
                    break;
            }
            if (progress >= 0 && progress <= 100) {
                if (mProgressListener != null) {
                    mProgressListener.onProgress(progress);
                }
                invalidate();
                postDelayed(progressChangeTask, timeMillis / 60);
            } else {
                progress = validateProgress(progress);
            }
        }
    };
    private int circleSolidColor;//实心的颜色
    private int circleBorderColor;//边框的颜色
    private int circleBorderWidth = 4;//边框的宽度
    private int circleRadius;//圆的半径
    private int progressColor;//进度条的颜色
    private int progressWidth;//进度条的宽度
    private int textColor;//文字的颜色

    private Rect mBounds;
    private Paint mPaint;
    private RectF mArcRectF;
    private int mCenterX;
    private int mCenterY;

    private String mText = "跳过";
    private long timeMillis = 3000;
    private OnProgressListener mProgressListener;
    private int progress = 100;
    private ProgressType mProgressType = ProgressType.COUNT_BACK;

    public void setProgressType(ProgressType progressType) {
        this.mProgressType = progressType;
        resetProgress();
        invalidate();
    }

    public ProgressType getProgressType() {
        return this.mProgressType;
    }

    public CountDownProgressView(Context context) {
        super(context);
        init();
    }

    public CountDownProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownProgress);
        if (typedArray != null) {
            if (typedArray.hasValue(R.styleable.CountDownProgress_circleSolidColor)) {
                circleSolidColor = typedArray.getColor(R.styleable.CountDownProgress_circleSolidColor, 0);
            } else {
                circleSolidColor = typedArray.getColor(R.styleable.CountDownProgress_circleSolidColor, Color.parseColor("#D3D3D3"));
            }
            if (typedArray.hasValue(R.styleable.CountDownProgress_circleBorderColor)) {
                circleBorderColor = typedArray.getColor(R.styleable.CountDownProgress_circleBorderColor, 0);
            } else {
                circleBorderColor = typedArray.getColor(R.styleable.CountDownProgress_circleBorderColor, Color.parseColor("#A9A9A9"));
            }
            if (typedArray.hasValue(R.styleable.CountDownProgress_textColor)) {
                textColor = typedArray.getColor(R.styleable.CountDownProgress_textColor, 0);
            } else {
                textColor = typedArray.getColor(R.styleable.CountDownProgress_textColor, Color.parseColor("#FFFFFF"));
            }
            if (typedArray.hasValue(R.styleable.CountDownProgress_progressColor)) {
                progressColor = typedArray.getColor(R.styleable.CountDownProgress_progressColor, 0);
            } else {
                progressColor = typedArray.getColor(R.styleable.CountDownProgress_progressColor, Color.parseColor("#0000FF"));
            }
            typedArray.recycle();
        }
    }

    public void init() {
        mPaint = new Paint();
        mBounds = new Rect();
        mArcRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width > height) {
            height = width;
        } else {
            width = height;
        }
        circleRadius = width / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getDrawingRect(mBounds);

        mCenterX = mBounds.centerX();
        mCenterY = mBounds.centerY();

        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(circleSolidColor);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), circleRadius, mPaint);

        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(circleBorderWidth);
        mPaint.setColor(circleBorderColor);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), circleRadius - circleBorderWidth, mPaint);

        Paint textPaint = getPaint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
//        float textY = mCenterY + (textPaint.descent() + Math.abs(textPaint.ascent())) / 2;
        int textY = (mBounds.bottom + mBounds.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(mText, mCenterX, textY, textPaint);

        mPaint.setColor(progressColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(progressWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcRectF.set(mBounds.left + progressWidth, mBounds.top + progressWidth,
                mBounds.right - progressWidth, mBounds.bottom - progressWidth);
        canvas.drawArc(mArcRectF, -90, 360 * progress / 100, false, mPaint);
    }

    public void start() {
        stop();
        post(progressChangeTask);
    }

    public void stop() {
        removeCallbacks(progressChangeTask);
    }

    public void reStart() {
        resetProgress();
        start();
    }

    private void resetProgress() {
        switch (mProgressType) {
            case COUNT:
                progress = 0;
                break;
            case COUNT_BACK:
                progress = 100;
                break;
        }
    }

    private int validateProgress(int progress) {
        if (progress > 100) {
            progress = 100;
        } else if (progress < 0) {
            progress = 0;
        }
        return progress;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
        invalidate();
    }

    public void setOnProgressListener(OnProgressListener progressListener) {
        this.mProgressListener = progressListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (Math.abs(x - mBounds.centerX()) <= circleRadius * 2
                        && Math.abs(y - mBounds.centerY()) <= circleRadius * 2) {
                    Timber.tag("CountDownProgressView").e("------------------onTouchEvent------------------");
                }
                break;
            default:
        }
        return super.onTouchEvent(event);
    }

    public enum ProgressType {COUNT, COUNT_BACK;}

    public interface OnProgressListener {
        void onProgress(int progress);
    }
}
