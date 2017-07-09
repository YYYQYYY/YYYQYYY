package com.yuqinyidev.android.framework.widget.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by RDX64 on 2017/6/30.
 */

public class ScaleInAnimation implements BaseAnimation {
    private static final float DEFAULT_SCALE_FROM = .5F;
    private final float mFrom;

    public ScaleInAnimation() {
        this(DEFAULT_SCALE_FROM);
    }

    public ScaleInAnimation(float mFrom) {
        this.mFrom = mFrom;
    }

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1F);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1F);
        return new Animator[]{scaleX, scaleY};
    }
}
