package com.yuqinyidev.android.framework.integration;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.yuqinyidev.android.framework.base.delegate.AppDelegate;
import com.yuqinyidev.android.framework.di.module.GlobalConfigModule;

import java.util.List;

/**
 * 此接口可以给框架配置一些参数,需要实现类实现后,并在AndroidManifest中声明该实现类
 * Created by RDX64 on 2017/6/27.
 */

public interface ConfigModule {
    void applyOptions(Context context, GlobalConfigModule.Builder builder);

    void registerComponents(Context context, IRepositoryManager repositoryManager);

    void injectAppLifecycle(Context context, List<AppDelegate.Lifecycle> lifecycles);

    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles);

    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);
}
