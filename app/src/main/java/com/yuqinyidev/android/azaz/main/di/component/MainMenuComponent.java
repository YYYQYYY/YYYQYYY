package com.yuqinyidev.android.azaz.main.di.component;

import com.yuqinyidev.android.azaz.main.di.module.MainMenuModule;
import com.yuqinyidev.android.azaz.main.mvp.ui.fragment.FragmentMainHome;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.di.scope.FragmentScope;

import dagger.Component;

/**
 * Created by RDX64 on 2017/7/2.
 */
@FragmentScope
@Component(modules = MainMenuModule.class, dependencies = AppComponent.class)
public interface MainMenuComponent {
    void inject(FragmentMainHome fragmentMainHome);
}
