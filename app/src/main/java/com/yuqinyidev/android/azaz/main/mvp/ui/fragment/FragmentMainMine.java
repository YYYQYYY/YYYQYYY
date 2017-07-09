package com.yuqinyidev.android.azaz.main.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuqinyidev.android.azaz.R;
import com.yuqinyidev.android.framework.base.BaseFragment;
import com.yuqinyidev.android.framework.di.component.AppComponent;

/**
 * Created by RDX64 on 2017/6/22.
 */

public class FragmentMainMine extends BaseFragment {

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_discover, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void setData(Object data) {

    }
}
