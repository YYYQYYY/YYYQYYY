package com.yuqinyidev.android.azaz.demo.di.module;

import com.yuqinyidev.android.framework.di.scope.ActivityScope;
import com.yuqinyidev.android.azaz.demo.mvp.contract.UserContract;
import com.yuqinyidev.android.azaz.demo.mvp.model.UserModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by RDX64 on 2017/6/29.
 */
@Module
public class UserModule {
    private UserContract.View view;

    public UserModule(UserContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UserContract.View provideUserView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    UserContract.Model provideUserModel(UserModel model) {
        return model;
    }
}
