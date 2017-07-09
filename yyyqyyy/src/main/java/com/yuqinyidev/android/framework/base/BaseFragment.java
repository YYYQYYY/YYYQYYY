package com.yuqinyidev.android.framework.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.yuqinyidev.android.framework.base.delegate.IFragment;
import com.yuqinyidev.android.framework.mvp.presenter.IPresenter;

import javax.inject.Inject;

/**
 * Created by RDX64 on 2017/6/28.
 */

public abstract class BaseFragment<P extends IPresenter> extends RxFragment implements IFragment {
    protected final String TAG = this.getClass().getSimpleName();

    @Inject
    protected P mPresenter;

    public BaseFragment() {
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        this.mPresenter = null;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }
}
