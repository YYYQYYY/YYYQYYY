package com.yuqinyidev.android.azaz.main.di.module;

import com.yuqinyidev.android.azaz.main.mvp.contract.MainMenuContract;
import com.yuqinyidev.android.azaz.main.mvp.model.MainMenuModel;
import com.yuqinyidev.android.framework.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by RDX64 on 2017/7/2.
 */
@Module
public class MainMenuModule {
    private MainMenuContract.View view;

    public MainMenuModule(MainMenuContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    MainMenuContract.View provideMainMenuView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    MainMenuContract.Model provideMainMenuModel(MainMenuModel model) {
        return model;
    }
}
