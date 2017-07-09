package com.yuqinyidev.android.framework.base.delegate;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * Created by RDX64 on 2017/6/27.
 */

public interface ActivityDelegate extends Parcelable {
    String LAYOUT_LINEAR_LAYOUT = "LinearLayout";
    String LAYOUT_FRAME_LAYOUT = "FrameLayout";
    String LAYOUT_RELATIVE_LAYOUT = "RelativeLayout";
    String ACTIVITY_DELEGATE = "activity_delegate";

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroy();
}
