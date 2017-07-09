package com.yuqinyidev.android.framework.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;

import com.yuqinyidev.android.framework.base.App;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by RDX64 on 2017/6/27.
 */

public class ActivityDelegateImpl implements ActivityDelegate {
    private Activity mActivity;
    private IActivity iActivity;
    private Unbinder mUnbinder;

    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (iActivity.useEventBus()) {
            EventBus.getDefault().register(mActivity);
        }
        iActivity.setupActivityComponent(((App) mActivity.getApplication()).getAppComponent());
        try {
            int layoutResourceId = iActivity.initView(savedInstanceState);
            if (layoutResourceId != 0) {
                mActivity.setContentView(layoutResourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mUnbinder = ButterKnife.bind(mActivity);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        if (iActivity != null && iActivity.useEventBus()) {
            EventBus.getDefault().unregister(mActivity);
        }
        this.mUnbinder = null;
        this.iActivity = null;
        this.mActivity = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected ActivityDelegateImpl(Parcel in) {
        this.mActivity = in.readParcelable(Activity.class.getClassLoader());
        this.iActivity = in.readParcelable(IActivity.class.getClassLoader());
        this.mUnbinder = in.readParcelable(Unbinder.class.getClassLoader());
    }

    public static final Creator<ActivityDelegateImpl> CREATOR = new Creator<ActivityDelegateImpl>() {
        @Override
        public ActivityDelegateImpl createFromParcel(Parcel source) {
            return new ActivityDelegateImpl(source);
        }

        @Override
        public ActivityDelegateImpl[] newArray(int size) {
            return new ActivityDelegateImpl[size];
        }
    };
}
