package com.yuqinyidev.android.framework.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yuqinyidev.android.framework.base.delegate.IActivity;
import com.yuqinyidev.android.framework.mvp.presenter.IPresenter;

import javax.inject.Inject;

import static com.yuqinyidev.android.framework.utils.ThirdViewUtil.convertAutoView;

/**
 * Created by RDX64 on 2017/6/28.
 */

public abstract class BaseActivity<P extends IPresenter> extends RxAppCompatActivity implements IActivity {
    protected final String TAG = this.getClass().getSimpleName();

    @Inject
    protected P mPresenter;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = convertAutoView(name, context, attrs);
        return view == null ? super.onCreateView(name, context, attrs) : view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
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

    @Override
    public boolean useFragment() {
        return true;
    }
}
