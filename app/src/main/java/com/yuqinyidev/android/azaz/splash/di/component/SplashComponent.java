package com.yuqinyidev.android.azaz.splash.di.component;

import com.yuqinyidev.android.azaz.splash.di.module.SplashModule;
import com.yuqinyidev.android.azaz.splash.mvp.ui.activity.SplashActivity;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;

import dagger.Component;

/**
 * Created by RDX64 on 2017/6/29.
 */
@ActivityScope
@Component(modules = SplashModule.class, dependencies = AppComponent.class)
public interface SplashComponent {
    void inject(SplashActivity activity);
}
