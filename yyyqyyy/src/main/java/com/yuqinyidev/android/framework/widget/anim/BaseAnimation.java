package com.yuqinyidev.android.framework.widget.anim;

import android.animation.Animator;
import android.view.View;

/**
 * Created by RDX64 on 2017/6/30.
 */

public interface BaseAnimation {
    Animator[] getAnimators(View view);
}
