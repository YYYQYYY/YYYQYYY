package com.yuqinyidev.android.framework.widget;


import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewTreeObserver;

/**
 * 字体大小自适应TextView宽度。
 * Created by yuqy on 2017/8/1.
 */
public class ScaleTextView extends AppCompatTextView {
    public ScaleTextView(Context context) {
        this(context, null, 0);
    }

    public ScaleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 测量字符串的长度
                float measureWidth = getPaint().measureText(String.valueOf(getText()));
                // 得到TextView 的宽度
                int width = getWidth() - getPaddingLeft() - getPaddingRight();
                // 当前size大小
                float textSize = getTextSize();
                if (width < measureWidth) {
                    textSize = (width / measureWidth) * textSize;
                }
                // 注意，使用像素大小设置
                setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                if (Build.VERSION.SDK_INT < 16) {
//                    removeLayoutListenerPre16(getViewTreeObserver(), this);
//                } else {
//                    removeLayoutListenerPost16(getViewTreeObserver(), this);
//                }
            }
        });
    }

//    @SuppressWarnings("deprecation")
//    private void removeLayoutListenerPre16(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
//        observer.removeGlobalOnLayoutListener(listener);
//    }
//
//    @TargetApi(16)
//    private void removeLayoutListenerPost16(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
//        observer.removeOnGlobalLayoutListener(listener);
//    }

}
