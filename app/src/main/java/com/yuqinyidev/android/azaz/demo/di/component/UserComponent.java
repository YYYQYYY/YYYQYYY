package com.yuqinyidev.android.azaz.demo.di.component;

import com.yuqinyidev.android.azaz.demo.di.module.UserModule;
import com.yuqinyidev.android.azaz.demo.mvp.ui.activity.UserActivity;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.di.scope.ActivityScope;

import dagger.Component;

/**
 * Created by RDX64 on 2017/6/29.
 */
@ActivityScope
@Component(modules = UserModule.class, dependencies = AppComponent.class)
public interface UserComponent {
    void inject(UserActivity activity);
}
