package com.yuqinyidev.android.azaz.splash.di.module;

import com.yuqinyidev.android.azaz.splash.mvp.contract.SplashContract;
import com.yuqinyidev.android.azaz.splash.mvp.model.SplashModel;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by RDX64 on 2017/6/29.
 */
@Module
public class SplashModule {
    private SplashContract.View view;

    public SplashModule(SplashContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SplashContract.View provideSplashView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SplashContract.Model provideSplashModel(SplashModel model) {
        return model;
    }
}
