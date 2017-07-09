package com.yuqinyidev.android.framework.base.delegate;

import android.os.Bundle;

import com.yuqinyidev.android.framework.di.component.AppComponent;

/**
 * Created by RDX64 on 2017/6/27.
 */

public interface IActivity {
    void setupActivityComponent(AppComponent appComponent);

    boolean useEventBus();

    int initView(Bundle savedInstanceState);

    void initData(Bundle savedInstanceState);

    boolean useFragment();
}
