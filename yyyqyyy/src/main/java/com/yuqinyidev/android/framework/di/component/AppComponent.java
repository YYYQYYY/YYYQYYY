package com.yuqinyidev.android.framework.di.component;

import android.app.Application;

import com.google.gson.Gson;
import com.yuqinyidev.android.framework.base.delegate.AppDelegate;
import com.yuqinyidev.android.framework.di.module.AppModule;
import com.yuqinyidev.android.framework.di.module.ClientModule;
import com.yuqinyidev.android.framework.di.module.GlobalConfigModule;
import com.yuqinyidev.android.framework.integration.AppManager;
import com.yuqinyidev.android.framework.integration.IRepositoryManager;
import com.yuqinyidev.android.framework.widget.imageloader.ImageLoader;

import java.io.File;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Component;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import okhttp3.OkHttpClient;

/**
 * Created by RDX64 on 2017/6/26.
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {
    Application application();

    /*
    管理网络请求层,以及数据缓存层
     */
    IRepositoryManager repositoryManager();

    /*
    RxJava错误处理管理类
     */
    RxErrorHandler rxErrorHandler();

    OkHttpClient okHttpClient();

    /*
    图片管理器
     */
    ImageLoader imageLoader();

    /*
    Gson
     */
    Gson gson();

    /*
    缓存文件根目录
     */
    File cacheFile();

    /*
    管理所有Activity
     */
    AppManager appManager();

    /*
    用来存取一些整个App公用的小型数据
     */
    Map<String, Object> extras();

    /*
    注入AppDelegate
     */
    void inject(AppDelegate delegate);

}
