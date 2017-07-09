package com.yuqinyidev.android.framework.widget.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by RDX64 on 2017/6/30.
 */

public class SlideInLeftAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "translationX", -view.getMeasuredWidth(), 0)};
    }
}
