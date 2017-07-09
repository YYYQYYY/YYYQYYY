package com.yuqinyidev.android.framework.widget.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by RDX64 on 2017/6/30.
 */

public class SlideInTopAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "translationY", -view.getMeasuredHeight(), 0)};
    }
}
