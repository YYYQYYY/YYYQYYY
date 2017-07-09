package com.yuqinyidev.android.framework.base;

import android.app.Application;

import com.yuqinyidev.android.framework.base.delegate.AppDelegate;
import com.yuqinyidev.android.framework.di.component.AppComponent;

/**
 * Created by RDX64 on 2017/6/28.
 */

public class BaseApplication extends Application implements App {
    private AppDelegate mAppDelegate;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppDelegate = new AppDelegate(this);
        this.mAppDelegate.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onTerminate();
        }
    }

    @Override
    public AppComponent getAppComponent() {
        return mAppDelegate.getAppComponent();
    }
}
