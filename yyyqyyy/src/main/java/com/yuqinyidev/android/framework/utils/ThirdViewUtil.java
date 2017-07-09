package com.yuqinyidev.android.framework.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.yuqinyidev.android.framework.base.delegate.ActivityDelegate.LAYOUT_FRAME_LAYOUT;
import static com.yuqinyidev.android.framework.base.delegate.ActivityDelegate.LAYOUT_LINEAR_LAYOUT;
import static com.yuqinyidev.android.framework.base.delegate.ActivityDelegate.LAYOUT_RELATIVE_LAYOUT;

/**
 * Created by RDX64 on 2017/6/28.
 */

public class ThirdViewUtil {
    /**
     * 0 说明 AndroidManifest 里面没有使用 AutoLauout 的Meta,即不使用 AutoLayout,1 为有 Meta ,即需要使用
     */
    public static int USE_AUTO_LAYOUT = -1;

    private ThirdViewUtil() {
    }

    public static Unbinder bindTarget(Object target, Object source) {
        if (source instanceof Activity) {
            return ButterKnife.bind(target, (Activity) source);
        } else if (source instanceof View) {
            return ButterKnife.bind(target, (View) source);
        } else if (source instanceof Dialog) {
            return ButterKnife.bind(target, (Dialog) source);
        } else {
            return Unbinder.EMPTY;
        }
    }

    @Nullable
    public static View convertAutoView(String name, Context context, AttributeSet attrs) {
        if (USE_AUTO_LAYOUT == -1) {
            USE_AUTO_LAYOUT = 1;
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo;
            try {
                applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo == null || applicationInfo.metaData == null
                        || !applicationInfo.metaData.containsKey("design_width")
                        || !applicationInfo.metaData.containsKey("design_height")) {
                    USE_AUTO_LAYOUT = 0;
                }
            } catch (PackageManager.NameNotFoundException e) {
                USE_AUTO_LAYOUT = 0;
            }
        }
        if (USE_AUTO_LAYOUT == 0) {
            return null;
        }
        View view = null;
        if (LAYOUT_FRAME_LAYOUT.equals(name)) {
            view = new AutoFrameLayout(context, attrs);
        } else if (LAYOUT_LINEAR_LAYOUT.equals(name)) {
            view = new AutoLinearLayout(context, attrs);
        } else if (LAYOUT_RELATIVE_LAYOUT.equals(name)) {
            view = new AutoRelativeLayout(context, attrs);
        }
        return view;
    }
}
