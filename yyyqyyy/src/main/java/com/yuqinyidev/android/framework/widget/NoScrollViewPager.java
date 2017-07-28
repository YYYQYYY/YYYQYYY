package com.yuqinyidev.android.framework.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by RDX64 on 2017/6/30.
 */

public class NoScrollViewPager extends ViewPager {
    private boolean isEnableScroll;

    public void setEnableScroll(boolean enableScroll) {
        isEnableScroll = enableScroll;
    }

    public NoScrollViewPager(Context context) {
        this(context, null);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isEnableScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !isEnableScroll && super.onTouchEvent(ev);
    }

    /**
     * 取消滑页动画
     *
     * @param item
     */
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
