package com.yuqinyidev.android.framework.base.delegate;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import com.yuqinyidev.android.framework.base.App;
import com.yuqinyidev.android.framework.di.component.AppComponent;
import com.yuqinyidev.android.framework.di.component.DaggerAppComponent;
import com.yuqinyidev.android.framework.di.module.AppModule;
import com.yuqinyidev.android.framework.di.module.ClientModule;
import com.yuqinyidev.android.framework.di.module.GlobalConfigModule;
import com.yuqinyidev.android.framework.integration.ActivityLifecycle;
import com.yuqinyidev.android.framework.integration.ConfigModule;
import com.yuqinyidev.android.framework.integration.ManifestParser;
import com.yuqinyidev.android.framework.widget.imageloader.glide.GlideImageConfig;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by RDX64 on 2017/6/27.
 */

public class AppDelegate implements App {
    private final List<ConfigModule> mModules;

    @Inject
    protected ActivityLifecycle mActivityLifecycle;

    private Application mApplication;
    private AppComponent mAppComponent;
    private List<Lifecycle> mAppLifecycles = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();
    private ComponentCallbacks2 mComponentCallbacks;

    public AppDelegate(Application application) {
        this.mApplication = application;
        this.mModules = new ManifestParser(mApplication).parse();
        for (ConfigModule module : mModules) {
            module.injectAppLifecycle(mApplication, mAppLifecycles);
            module.injectActivityLifecycle(mApplication, mActivityLifecycles);
        }
    }

    public void onCreate() {
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(mApplication))
                .clientModule(new ClientModule())
                .globalConfigModule(getGlobalConfigModule(mApplication, mModules))
                .build();
        mAppComponent.inject(this);
        mAppComponent.extras().put(ConfigModule.class.getName(), mModules);
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);
        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }
        for (ConfigModule module : mModules) {
            module.registerComponents(mApplication, mAppComponent.repositoryManager());
        }
        for (Lifecycle lifecycle : mAppLifecycles) {
            lifecycle.onCreate(mApplication);
        }
        mComponentCallbacks = new AppComponentCallbacks(mApplication, mAppComponent);
        mApplication.registerComponentCallbacks(mComponentCallbacks);

        LitePal.initialize(mApplication);
    }

    public void onTerminate() {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mComponentCallbacks != null) {
            mApplication.unregisterComponentCallbacks(mComponentCallbacks);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mAppLifecycles != null && mAppLifecycles.size() > 0) {
            for (Lifecycle lifecycle : mAppLifecycles) {
                lifecycle.onTerminate(mApplication);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycles = null;
        this.mComponentCallbacks = null;
        this.mAppLifecycles = null;
        this.mApplication = null;
    }

    private GlobalConfigModule getGlobalConfigModule(Application application, List<ConfigModule> modules) {
        GlobalConfigModule.Builder builder = GlobalConfigModule.builder();
        for (ConfigModule module : modules) {
            module.applyOptions(application, builder);
        }
        return builder.build();
    }

    @Override
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public interface Lifecycle {
        void onCreate(Application application);

        void onTerminate(Application application);
    }

    private static class AppComponentCallbacks implements ComponentCallbacks2 {
        private Application mApplication;
        private AppComponent mAppComponent;

        public AppComponentCallbacks(Application application, AppComponent appComponent) {
            this.mApplication = application;
            this.mAppComponent = appComponent;
        }

        @Override
        public void onTrimMemory(int level) {

        }

        @Override
        public void onConfigurationChanged(Configuration configuration) {

        }

        /**
         * 内存不足时，清理缓存
         */
        @Override
        public void onLowMemory() {
            mAppComponent.imageLoader().clear(mApplication, GlideImageConfig.builder().isClearMemory(true).build());
        }
    }
}
