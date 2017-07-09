package com.yuqinyidev.android.framework.base.delegate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuqinyidev.android.framework.di.component.AppComponent;

/**
 * Created by RDX64 on 2017/6/27.
 */

public interface IFragment {
    void setupFragmentComponent(AppComponent appComponent);

    boolean useEventBus();

    View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    void initData(Bundle savedInstanceState);

    void setData(Object data);
}
